package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerMVCTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController vetController;

    @Mock
    Map<String, Object> modelMock;

    List<Vet> vetList = new ArrayList<>();

    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(vetController).build();
        vetList.add(new Vet());
        given(clinicService.findVets()).willReturn(vetList);
    }

    @Test
    void testControllerShowVetList() throws Exception {
        mockMvc.perform(get(("/vets.html")))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
    }

    @Test
    void showVetList() {
        //given
        final String RET_URL = "vets/vetList";
        //when
        String webView = vetController.showVetList(modelMock);
        //then
        then(clinicService).should().findVets();
        assertEquals(RET_URL,webView);
    }

    @Test
    void showResourcesVetList() {
        //when
        Vets vets =  vetController.showResourcesVetList();
        //then
        then(clinicService).should().findVets();
        assertTrue(vets.getVetList().isEmpty()==false,"La lista está vacía");
    }
}