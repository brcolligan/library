package com.techelevator.toolLibrary.model;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.techelevator.Loan;
import com.techelevator.Tool;


@Component
public class LoanDAO {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public LoanDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	public void saveLoanItem(Loan newLoan) {
		
		Date loanEndDate=null;
		Double lateFee = 0d;
		Double maintenanceFee = 0d;
		Double cleaningFee = 0d;
		
		String insertSQL = "INSERT INTO loan(loan_id, inventory_id, tool_loaned, loan_start_date, loan_due_date, loan_end_date, first_name, last_name, phone_number, license_num, late_fee, maintenance_fee, cleaning_fee VALUES (nextval('seq_loan_id'), ?, ?, ?, ?, ?, ?, ?;";
	
		jdbcTemplate.update(insertSQL, 	newLoan.getLoanId(),
										newLoan.getInventoryId(),
										newLoan.getToolLoaned(),
										newLoan.getDateOfLoan(),
										newLoan.getExpectedReturn(),
										loanEndDate,
										newLoan.getFirstName(),
										newLoan.getLastName(),
										newLoan.getPhoneNumber(),
										newLoan.getDriversLicense(),
										lateFee,
										maintenanceFee,
										cleaningFee);
		
		String updateSQL = "UPDATE tool_inventory SET tool_available = 'f' WHERE inventory_id = ?;";
		
		jdbcTemplate.update(updateSQL, newLoan.getInventoryId());
	}
	
	
	
	 public Loan getLoanById(int loanId) {
		Loan existingLoan = null;
		String selectSQL = "SELECT * FROM loan WHERE loan_id = ?;";
		
		jdbcTemplate.update(selectSQL, loanId);
		 
		SqlRowSet results = jdbcTemplate.queryForRowSet(selectSQL, loanId);
		if(results.next()) {
			existingLoan.setLoanId(results.getInt("loan_id"));
			existingLoan.setInventoryId(results.getInt("inventory_id"));
			existingLoan.setFirstName(results.getString("firstName"));
			existingLoan.setLastName(results.getString("lastName"));
			existingLoan.setDateOfLoan(results.getDate("loan_start_date"));
			existingLoan.setExpectedReturn(results.getDate("loan_due_date"));
			existingLoan.setDriversLicense(results.getString("driversLicense"));
			existingLoan.setPhoneNumber(results.getString("phoneNumber"));
			existingLoan.setToolLoaned(results.getString("toolLoaned"));				
		}
		
		 return existingLoan; 
	 }
	
	
	public List<Loan> getListOfLoans(){ //exclude loans that are complete
		List<Loan> loanList = new ArrayList<>();
		Loan existingLoan = new Loan(0, 0, null, null, null, null, null, null, null, null);
		
		String selectSQL = "SELECT * FROM loan WHERE loan_end_date IS NULL;";
		
		jdbcTemplate.update(selectSQL);
		 
		SqlRowSet results = jdbcTemplate.queryForRowSet(selectSQL);
		if(results.next()) {
				existingLoan.setLoanId(results.getInt("loan_id"));
				existingLoan.setInventoryId(results.getInt("inventory_id"));
				existingLoan.setFirstName(results.getString("firstName"));
				existingLoan.setLastName(results.getString("lastName"));
				existingLoan.setDateOfLoan(results.getDate("loan_start_date"));
				existingLoan.setExpectedReturn(results.getDate("loan_due_date"));
				existingLoan.setDriversLicense(results.getString("driversLicense"));
				existingLoan.setPhoneNumber(results.getString("phoneNumber"));
				existingLoan.setToolLoaned(results.getString("toolLoaned"));				
		
				loanList.add(existingLoan);
		}
		return loanList;
	}
	
	
	
	public void updateReturnedItem(){
	
		// update tool_inventory.tool_available to 't'
		// update loan.loan_end_date to current_date
		// update loan.late_fee, loan.maintenance_fee, loan.cleaning_fee as needed
	
	}
	
	
	public List<Tool> getListOfAvailableTools(){
		
		Tool foundTool = null;
		List<Tool> availableToolList = new ArrayList<>();
		String selectSQL =  "SELECT tool.tool_id as toolId, tool.name as toolName, tool.tool_category_id as toolCategoryId, tool.description as toolDescription, tool.loan_period_in_days as toolLoanPeriod, tool_category.name as toolCategoryName, tool_inventory.tool_inventory_id as toolInventoryId FROM tool INNER JOIN tool_inventory ON tool.tool_id = tool_inventory.tool_id inner join tool_category on tool.tool_category_id = tool_category.tool_category_id WHERE tool_available = 'T' ORDER BY toolName";
		SqlRowSet results = jdbcTemplate.queryForRowSet(selectSQL);
		while(results.next()) {
			String toolName = results.getString("toolName");
			String toolDescription = results.getString("toolDescription");
			int toolLoanPeriod = results.getInt("toolLoanPeriod");
			int toolId = results.getInt("toolId");		
			int toolCategoryId = results.getInt("toolCategoryId");
			String toolCategoryName = results.getString("toolCategoryName");
			int toolInventoryId = results.getInt("toolInventoryId");
			
			foundTool = new Tool (toolName, toolDescription, toolLoanPeriod, toolId, toolCategoryId, toolCategoryName, toolInventoryId);
			availableToolList.add(foundTool);
		}		
		return availableToolList;
	}
	
	public Tool getToolByInventoryId(int toolInventoryId) {
		
		Tool foundTool = null;
		String selectSQL = "SELECT tool.tool_id as toolId, tool.name as toolName, tool.tool_category_id as toolCategoryId, tool.description as toolDescription, tool.loan_period_in_days as toolLoanPeriod, tool_category.name as toolCategoryName, tool_inventory.tool_inventory_id as toolInventoryId FROM tool INNER JOIN tool_inventory ON tool.tool_id = tool_inventory.tool_id inner join tool_category on tool.tool_category_id = tool_category.tool_category_id WHERE tool_inventory.tool_inventory_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(selectSQL, toolInventoryId);
		if(results.next()) {
			String toolName = results.getString("toolName");
			String toolDescription = results.getString("toolDescription");
			int toolLoanPeriod = results.getInt("toolLoanPeriod");
			int toolId = results.getInt("toolId");		
			int toolCategoryId = results.getInt("toolCategoryId");
			String toolCategoryName = results.getString("toolCategoryName");
			toolInventoryId = results.getInt("toolInventoryId");
			
			foundTool = new Tool (toolName, toolDescription, toolLoanPeriod, toolId, toolCategoryId, toolCategoryName, toolInventoryId);
			
		}
		return foundTool;
	}

}
