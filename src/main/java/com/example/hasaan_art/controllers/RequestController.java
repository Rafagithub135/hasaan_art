package com.example.hasaan_art.controllers;

import com.example.hasaan_art.models.Art;
import com.example.hasaan_art.models.Customer;
import com.example.hasaan_art.models.Request;
import com.example.hasaan_art.repositories.ArtRepository;
import com.example.hasaan_art.repositories.CustomerRepository;
import com.example.hasaan_art.repositories.RequestsRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestsController {
    private final ArtRepository artDao;
    private final CustomerRepository customerDao;
    private final RequestsRepository requestDao;

    public RequestsController(ArtRepository artDao, CustomerRepository customerDao, RequestsRepository requestDao) {
        this.artDao = artDao;
        this.customerDao = customerDao;
        this.requestDao = requestDao;
    }

    @GetMapping("/requests")
    public String showRequests(Model model) {
        return "requests/index";
    }

    @PostMapping("/requests/create")
    public String createRequest() {
        return "redirect:/requests";
    }

    @PostMapping("/art/{id}/admin/{requestId}")
    public String adminRequestControl(@PathVariable long id, @PathVariable long requestId, @RequestParam String status) {
        Art art = artDao.findArtById(id);
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestDao.findRequestById(requestId);
        if (!customer.isAdmin() || request == null || !request.getArt().equals(art)) {
            return "redirect:/art";
        }

        if (status.equals("accept")) {
            request.setStatus("Accepted");
            request.getCustomer().setArt(art);
            customerDao.save(request.getCustomer());
        } else if (status.equals("decline")) {
            request.setStatus("Declined");
        }
        requestDao.delete(request);
        return "redirect:/art/" + id + "/admin";
    }

    @PostMapping("/art/{id}/requests/{requestId}/cancel")
    public String cancelRequest(@PathVariable long id, @PathVariable long requestId) {
        Art art = artDao.findArtById(id);
        Customer customer = customerDao.findByCustomer(SecurityContextHolder.getContext().getAuthentication().getName());
        Request request = requestDao.findRequestById(requestId);

        if (request == null || !request.getCustomer().equals(customer) || !request.getArt().equals(art)) {
            return "redirect:/art";
        }

        requestDao.delete(request);
        return "redirect:/art";
    }
}