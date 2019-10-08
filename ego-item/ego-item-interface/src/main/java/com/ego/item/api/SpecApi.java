package com.ego.item.api;

import com.ego.item.pojo.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("spec")
public interface SpecApi {

    @GetMapping("{cid}")
    public ResponseEntity<String> querySpecByCid(@PathVariable("cid") Long cid);


    @PostMapping
    public ResponseEntity<Void> save(Specification specification);
}
