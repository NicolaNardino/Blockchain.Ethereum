pragma solidity ^0.4.0;

contract BaseCoinManager {
    address public owner;
    string message;
    string coinName;
    
    function BaseCoinManager(string _coinName) public { 
    	owner = msg.sender; 
    	coinName = _coinName;
    }
    
    function getCoinName() public constant returns (string) {
    	return coinName;
    }
    
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

	function CoinManager(string _coinName) BaseCoinManager(_coinName) public { 
    }
    
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