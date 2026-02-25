package com.example.IMS.controller;

import com.example.IMS.dto.RetailerRegistrationDto;
import com.example.IMS.dto.VendorRegistrationDto;
import com.example.IMS.dto.InvestorRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    // Retailer Registration
    @GetMapping("/retailer")
    public String showRetailerRegistrationForm(Model model) {
        model.addAttribute("retailerDto", new RetailerRegistrationDto());
        return "auth/register-retailer";
    }
    
    @PostMapping("/retailer")
    public String registerRetailer(
            @Valid @ModelAttribute("retailerDto") RetailerRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register-retailer";
        }
        
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.retailerDto", "Passwords do not match");
            return "auth/register-retailer";
        }
        
        try {
            // TODO: Implement retailer registration service
            // retailerService.registerRetailer(dto);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration submitted successfully! Your account is pending verification.");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("email", "error.retailerDto", e.getMessage());
            return "auth/register-retailer";
        }
    }
    
    // Vendor Registration
    @GetMapping("/vendor")
    public String showVendorRegistrationForm(Model model) {
        model.addAttribute("vendorDto", new VendorRegistrationDto());
        return "auth/register-vendor";
    }
    
    @PostMapping("/vendor")
    public String registerVendor(
            @Valid @ModelAttribute("vendorDto") VendorRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register-vendor";
        }
        
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.vendorDto", "Passwords do not match");
            return "auth/register-vendor";
        }
        
        try {
            // TODO: Implement vendor registration service
            // vendorService.registerVendor(dto);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration submitted successfully! Your account is pending verification.");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("email", "error.vendorDto", e.getMessage());
            return "auth/register-vendor";
        }
    }
    
    // Investor Registration
    @GetMapping("/investor")
    public String showInvestorRegistrationForm(Model model) {
        model.addAttribute("investorDto", new InvestorRegistrationDto());
        return "auth/register-investor";
    }
    
    @PostMapping("/investor")
    public String registerInvestor(
            @Valid @ModelAttribute("investorDto") InvestorRegistrationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register-investor";
        }
        
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.investorDto", "Passwords do not match");
            return "auth/register-investor";
        }
        
        try {
            // TODO: Implement investor registration service
            // investorService.registerInvestor(dto);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration submitted successfully! Your account is pending verification.");
            return "redirect:/login";
        } catch (Exception e) {
            result.rejectValue("email", "error.investorDto", e.getMessage());
            return "auth/register-investor";
        }
    }
}
