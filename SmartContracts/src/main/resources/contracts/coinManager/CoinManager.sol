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
    
    function getOwner() public constant returns (address) {
    	return owner;	
    }
    
    function kill() public { if (msg.sender == owner) selfdestruct(owner); }
}

contract CoinManager is BaseCoinManager {
    mapping (address => uint) public balances;
    mapping (address => uint) public weiDeposits;
	
    event Sent(address from, address to, uint amount);
    event Mint(address from, address to, uint amount);
    event WeiDeposited(address from, uint amount, uint balance);

	function CoinManager(string _coinName) BaseCoinManager(_coinName) public { 
    }
    
    function mint(address receiver, uint amount) public {
        if (msg.sender != owner) return;
        balances[receiver] += amount;
        emit Mint(msg.sender, receiver, amount);
    }

    function send(address receiver, uint amount) public {
        if (balances[msg.sender] < amount) 
        	return;
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        emit Sent(msg.sender, receiver, amount);
    }
    
    function getBalance() view public returns (uint) {
    	return address(this).balance;
    }
    
    function() payable {
        weiDeposits[msg.sender] +=msg.value;
        emit WeiDeposited(msg.sender, msg.value, address(this).balance);
    }
}