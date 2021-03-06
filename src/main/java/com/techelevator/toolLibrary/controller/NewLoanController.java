package com.techelevator.toolLibrary.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.toolLibrary.model.Loan;
import com.techelevator.toolLibrary.model.LoanDAO;
import com.techelevator.toolLibrary.model.Tool;

@Controller
public class NewLoanController {
	private LoanDAO loanDAO;
	
	@Autowired
	public NewLoanController(LoanDAO loanDAO) {
		this.loanDAO = loanDAO;
	}

		@RequestMapping( path="/addTools")
		public String showInventoryList(Map <String, Object> model) {
			List <Tool> toolList = loanDAO.getListOfAvailableTools();
			model.put("toolList", toolList);
			return "inventoryList";  
		}
		
		@RequestMapping( path={"/addToCart"})
		public String addToCart(@RequestParam("toolInventoryId") int toolInventoryId, HttpSession session) {
			
			Tool addedTool = loanDAO.getToolByInventoryId(toolInventoryId);
			List<Tool> shoppingCart = new ArrayList<>();
			if (session.getAttribute("shoppingCart") == null)  {
				session.setAttribute("shoppingCart", new ArrayList <>());
			}
				shoppingCart = (List<Tool>)session.getAttribute("shoppingCart");
				shoppingCart.add(addedTool);
			return "cartView";  
		}
		
		
		@RequestMapping( path={"/viewCart"} )
		public String viewLoan() {
			return "cartView";  
		}
		
		@RequestMapping( path={"/removeFromCart"} )
		public String removeFromCart(@RequestParam("toolInventoryIndex") int toolInventoryIndex, HttpSession session) {
			
			((List<Tool>) session.getAttribute("shoppingCart")).remove(toolInventoryIndex);
			return "cartView";  
		}
		
		@RequestMapping( path={"/checkoutTools"} )
		public String processLoan(HttpSession session, @RequestParam("firstName") String firstName, 
														@RequestParam("lastName") String lastName,
														@RequestParam("license") String licenseNum,
														@RequestParam("phone") String phone
													//	@RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date
														)
														{

			List<Tool> shoppingCart = (List<Tool>)session.getAttribute("shoppingCart");
			LocalDate todaysDate = LocalDate.now();
			for(Tool toolItem : shoppingCart) {
				 Loan newLoan = new Loan();
				 newLoan.setDateOfLoan(todaysDate);
				 newLoan.setDriversLicense(licenseNum);
				newLoan.setExpectedReturn(toolItem.getToolDueDate());
				
			
				 newLoan.setFirstName(firstName);
				 newLoan.setInventoryId(toolItem.getToolInventoryId());
				 newLoan.setLastName(lastName);
				 newLoan.setPhoneNumber(phone);
				 newLoan.setToolCategoryName(toolItem.getToolCategoryName());
				 newLoan.setToolLoaned(toolItem.getToolName());
				loanDAO.saveLoanItem(newLoan);
			 }
				
			//accept form submission
			//change value of tool_inventory.inventory_availability
			//insert new loan row in DB
				
			session.invalidate();
			return "loanProcess";  
		}
		
		@RequestMapping( path={"/reviewLoan"} )
		public String reviewLoan() {
			return "redirect: homePage";  
		}
		@RequestMapping( path={"/clearCart"} )
		public String clearCart (HttpSession session){
			session.invalidate();
			return "cartView";  
		}
	}