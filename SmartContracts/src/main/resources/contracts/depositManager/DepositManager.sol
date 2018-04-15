pragma solidity ^0.4.0;

contract DepositManager {
 	address public owner;
	mapping (address => uint) public weiDeposits;
	event WeiReceived(address from, uint amount, uint balance);
	
	function DepositManager() public { 
    	owner = msg.sender; 
    }
    
	function deposit(address sender) public payable {
        weiDeposits[sender] +=msg.value;
        emit WeiReceived(sender, msg.value, weiDeposits[sender]);
    }
    
    function getBalance() view public returns (uint) {
    	return address(this).balance;
    }
    
    function kill() public { 
    	if (msg.sender == owner) selfdestruct(owner); 
    }
}