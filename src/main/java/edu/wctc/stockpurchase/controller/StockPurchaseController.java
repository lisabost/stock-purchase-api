package edu.wctc.stockpurchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.JstlUtils;
import edu.wctc.stockpurchase.exception.*;

import java.util.List;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {

    private StockPurchaseService service;

    @Autowired
    public StockPurchaseController(StockPurchaseService sps) {
        this.service = sps;
    }

    // Get all stock purchases - This works in postman
    @GetMapping
    public List<StockPurchase> getPurchases() {
        return service.getAllPurchases();
    }

    // This allows you to add a stock purchase using a JSON string in the body of the request - this works in postman
    @PostMapping
    public StockPurchase createPurchase (@RequestBody StockPurchase newPurchase) {
        newPurchase.setId(0);
        return service.save(newPurchase);

    }

    // This deletes the selected stock purchase by id - This works in postman
    @DeleteMapping("/{deleteId}")
    public String delete(@PathVariable String deleteId) {
        try {
            int id = Integer.parseInt(deleteId);
            service.delete(id);
            return "Purchase deleted: ID " + id;
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Purchase ID must be a number", e);
        } catch (edu.wctc.stockpurchase.exception.ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        }
    }

    // This takes in a JSON string of instructions and updates an entry based on those instructions - this works in postman
    @PatchMapping("/{patchId}")
    public StockPurchase patchStudent(@PathVariable String patchId,
                                @RequestBody JsonPatch patch) {
        try {
            int id = Integer.parseInt(patchId);
            return service.patch(id, patch);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Purchase Id must be a number", e);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid patch format: " + e.getMessage(), e);
        }
    }


    // This takes a stock purchase from the body and updates its matching stock purchase - this works in postman
    @PutMapping
    public StockPurchase updateStudent(@RequestBody StockPurchase purchase) {
        try {
            return service.update(purchase);
        } catch (edu.wctc.stockpurchase.exception.ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    e.getMessage(), e);
        }
    }

    // Get one stock purchase by id - this works in postman
    @GetMapping("/{incomingId}")
    public StockPurchase getById(@PathVariable String incomingId) {
        try {
            int id = Integer.parseInt(incomingId);
            return service.getById(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Purchase ID must be a number", e);
        }
    }


}
