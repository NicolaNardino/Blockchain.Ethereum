pragma solidity ^0.4.0;

contract DepositManager {
 	
	function deposit(address sender) public payable {
    }
}

contract BaseCoinManager {
    address public owner;
    string message;
    string coinName;
    
    function BaseCoinManager(string _coinName) public { 
    	owner = msg.sender; 
    	coinName = _coinName;
    }
    
    function getCoinName() public view returns (string) {
    	return coinName;
    }
    
    function getMessage() public view returns (string) {
    	return message;
    }
    
    function setMessage(string _message) public {
        message = _message;
    }
    
    function kill() public { 
    	if (msg.sender == owner) selfdestruct(owner); 
    }
}

contract CoinManager is BaseCoinManager {
    mapping (address => uint) public balances;
    mapping (address => uint) public weiDeposits;
    DepositManager depositManager;
	
    event Sent(address from, address to, uint amount);
    event Mint(address from, address to, uint amount);
    event WeiDeposited(address from, uint amount, uint balance);
    event SentToDepositManager(address from, uint amount);

	function CoinManager(string _coinName, address _depositManager) BaseCoinManager(_coinName) public {
		depositManager = DepositManager(_depositManager); 
    }
    
    function mint(address receiver, uint amount) public {
        require(msg.sender != owner);
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
    
    function() public payable {
        weiDeposits[msg.sender] +=msg.value;
        emit WeiDeposited(msg.sender, msg.value, address(this).balance);
    }
    
    function sendToDepositManager() public payable {
        depositManager.deposit.value(msg.value)(msg.sender);
        emit SentToDepositManager(msg.sender, msg.value);
    }
}