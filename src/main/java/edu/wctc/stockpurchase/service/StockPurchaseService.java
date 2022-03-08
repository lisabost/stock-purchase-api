package edu.wctc.stockpurchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.wctc.stockpurchase.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StockPurchaseService {
    private StockPurchaseRepository repo;
    private ObjectMapper objectMapper;

    @Autowired
    public StockPurchaseService(StockPurchaseRepository spr, ObjectMapper om) {
        this.repo = spr;
        this.objectMapper = om;
    }


    //patch
    public StockPurchase patch(int id, JsonPatch patch) throws ResourceNotFoundException,
            JsonPatchException, JsonProcessingException {
        StockPurchase existingPurchase = getById(id);
        JsonNode patched = patch.apply(objectMapper
                .convertValue(existingPurchase, JsonNode.class));
        StockPurchase patchedPurchase = objectMapper.treeToValue(patched, StockPurchase.class);
        repo.save(patchedPurchase);
        return patchedPurchase;
    }

    public StockPurchase update(StockPurchase purchase)
            throws ResourceNotFoundException {
        if (repo.existsById(purchase.getId())) {
            return repo.save(purchase);
        } else {
            throw new ResourceNotFoundException("Stock Purchase", "id", purchase.getId());
        }
    }

    //delete
    public void delete(int id) throws ResourceNotFoundException {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
        else {
            throw new ResourceNotFoundException("Stock Purchase", "id", id);
        }
    }

    //save
    public StockPurchase save(StockPurchase purchase) {

        return repo.save(purchase);
    }
    //get all
    public List<StockPurchase> getAllPurchases() {
        List<StockPurchase> list = new ArrayList<>();
        repo.findAllByOrderById().forEach(list::add);
        return list;
    }
    //get one
    public StockPurchase getById (int id) {
        StockPurchase sp;
        Optional<StockPurchase> s = repo.findById(id);
        if (s.isPresent()) {
            return s.get();
        }
        else  {
            sp = new StockPurchase();
            sp.setId(id);
        }
        return sp;
    }







}
