package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml","classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    @Captor
    ArgumentCaptor<String> captor;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();

        given(clinicService.findOwnerByLastName(captor.capture())).willAnswer(invocationOnMock -> {
            List<Owner> ownerList = new ArrayList<>();
            String arg0 = invocationOnMock.getArgument(0);
            if(arg0.equals ("Baal")){
                Owner owner = new Owner();
                owner.setId(6);
                ownerList.add(owner);
            }else if(arg0.equals ("romero")){
                Owner owner = new Owner();
                owner.setId(6);
                ownerList.add(owner);
                Owner owner2 = new Owner();
                owner2.setId(7);
                ownerList.add(owner2);
            }
            return ownerList;
        });
    }

    @Test
    void testNewOwnerPostValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                .param("firstName","Jimmy")
                .param("lastName","Buffet")
                .param("Address","sunset drv")
                .param("city","kansas")
                .param("telephone","11545152"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testNewOwnerPostNotValid() throws Exception {
        mockMvc.perform(post("/owners/new")
                        .param("firstName","Jimmy")
                        .param("lastName","Buffet")
                        .param("Address","sunset drv")
                        .param("city","kansas"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","telephone"));


    }

    @Test
    void testNewOwnerUpdatealid() throws Exception {
        mockMvc.perform(post("/owners/{miPlaceholder}/edit",1)
                        .param("firstName","Jimmy")
                        .param("lastName","Buffet")
                        .param("Address","sunset drv")
                        .param("city","kansas")
                        .param("telephone","11545152"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name(VIEWS_OWNER_CREATE_OR_UPDATE_FORM));                ;
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners").param("lastName","dont finde me"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameOneResult() throws Exception {
        mockMvc.perform(get("/owners").param("lastName","Baal"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/6"));
    }

    @Test
    void testFindByNameSeveralResults() throws Exception {
//        given(clinicService.findOwnerByLastName("romero")).willReturn(
//                Arrays.asList(new Owner(),new Owner()));
        mockMvc.perform(get("/owners").param("lastName","romero"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));
        mockMvc.perform(get("/owners").param("lastName",""));
        assertEquals(captor.getValue(),"");
    }

}