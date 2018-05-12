package com.cash.keeper.controller;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Интеграционный тест на REST API
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexControllerIntegratioinTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Тест создания счета
     *
     * @throws Exception
     */
    @Test
    public void createAccountTest() throws Exception {
        MockHttpServletRequestBuilder form = MockMvcRequestBuilders
                .post("/api/account")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Входящие параметры
        form.param("fio", "Igor");
        form.param("currency", "USD");

        mockMvc.perform(form).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    /**
     * Тест пополнения счета
     *
     * @throws Exception
     */
    @Test
    public void fiveIncomeTest() throws Exception {
        MockHttpServletRequestBuilder form = MockMvcRequestBuilders
                .post("/api/income")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Входящие параметры
        form.param("accountId", "1");
        form.param("amount", "5.01");

        mockMvc.perform(form).andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Тест получения счета
     *
     * @throws Exception
     */
    @Test
    public void getAccountListTest() throws Exception {
        final String expectedResult = "[{'id':1,'fio':'Igor','balance':5.01,'currency':'USD'}]";
        MockHttpServletRequestBuilder form = MockMvcRequestBuilders
                .get("/api/accounts")
                .accept(MediaType.APPLICATION_JSON);

        ResultActions result = mockMvc.perform(form);
        result.andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertEquals(expectedResult, result.andReturn().getResponse().getContentAsString().replace("\"", "'"));
    }
}