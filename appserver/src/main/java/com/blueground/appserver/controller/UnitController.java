package com.blueground.appserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.blueground.appserver.exception.DataNotFound;
import com.blueground.appserver.jwt.JwtUtil;
import com.blueground.appserver.model.AuthenticationResponse;
import com.blueground.appserver.model.Unit;
import com.blueground.appserver.model.User;
import com.blueground.appserver.repository.UnitRepository;
import com.blueground.appserver.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author User Panagiotis Adamopoulos
 */
//@CrossOrigin(origins = "http://localhost:8383")
@RestController
@RequestMapping("/units")
public class UnitController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUti;

    @Autowired
    private UserDetailsServiceImpl myUserDetailsServices;


    @Autowired
    private UnitRepository unitRepository;

    @RequestMapping(path = "/sayhello")
    public String hello() {
        return "Hi, its works";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthentication(@RequestBody User user) throws Exception {

        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsServices.loadUserByUsername(user.getUsername());
        final String jwt = jwtUti.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(user.getUsername(), user.getUsername(),
                userDetails.getAuthorities().toString(), jwt));
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Unit>> lists(@RequestParam(required = false) String title) {

        try {
            List<Unit> datas = new ArrayList<Unit>();

            if (title == null) {
                unitRepository.findAll().forEach(datas::add);
            } else {
                unitRepository.findByTitleContaining(title).forEach(datas::add);
            }

            if (datas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(datas, HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Unit> geUnitById(@PathVariable("id") int id) {
        Optional<Unit> unitData = unitRepository.findById(id);

        if (unitData.isPresent()) {
            return new ResponseEntity<>(unitData.get(), HttpStatus.OK);
        } else {

            throw new DataNotFound("Not found id-" + id);

        }
    }

    @PostMapping(path = "/save")
    public ResponseEntity<Unit> save(@RequestBody Unit unit) {

        try {

            Unit responseUnit = new Unit();
            responseUnit.setTitle(unit.getTitle());
            responseUnit.setDescription(unit.getDescription());
            responseUnit.setPublished(false);

            unitRepository.save(responseUnit);

            return new ResponseEntity<>(responseUnit, HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable("id") int id, @RequestBody Unit unit) {
        Optional<Unit> unitData = unitRepository.findById(id);

        if (unitData.isPresent()) {
            Unit _unit = unitData.get();
            _unit.setTitle(unit.getTitle());
            _unit.setDescription(unit.getDescription());
            _unit.setPublished(unit.isPublished());
            return new ResponseEntity<>(unitRepository.save(_unit), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUnit(@PathVariable("id") int id) {
        try {
            unitRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/published")
    public ResponseEntity<List<Unit>> findByPublished() {
        try {
            List<Unit> units = unitRepository.findByPublished(true);

            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}