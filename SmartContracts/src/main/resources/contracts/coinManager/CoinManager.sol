pragma solidity ^0.4.0;

contract BaseCoinManager {
    address public owner;
    string message;
    
    function BaseCoinManager() { owner = msg.sender; }
    
    function getMessage() public constant returns (string) {
    	return message;
    }
    
    function setMessage(string _message) public {
        message = _message;
    }
    
    function kill() public { if (msg.sender == owner) selfdestruct(owner); }
}

contract CoinManager is BaseCoinManager {
    mapping (address => uint) public balances;

    event Sent(address from, address to, uint amount);
    event Mint(address from, address to, uint amount);

    function mint(address receiver, uint amount) public {
        if (msg.sender != owner) return;
        balances[receiver] += amount;
        Mint(msg.sender, receiver, amount);
    }

    function send(address receiver, uint amount) public {
        if (balances[msg.sender] < amount) 
        	return;
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        Sent(msg.sender, receiver, amount);
    }
}