package com.zenika.academy.woodblocktoys;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zenika.academy.woodblocktoys.Account.Account;
import com.zenika.academy.woodblocktoys.Barrel.Barrel;
import com.zenika.academy.woodblocktoys.Color.Color;
import com.zenika.academy.woodblocktoys.Height.Height;
import com.zenika.academy.woodblocktoys.Paint.Paint;
import com.zenika.academy.woodblocktoys.Shape.Shape;
import com.zenika.academy.woodblocktoys.Wood.Wood;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "dev")
@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BlocksApplicationTests {

    /************************VARIABLES************************/
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    /************************HELPER OBJECTS************************/
    private Color color1 = Color.builder()
            .id(1L)
            .type("blue")
            .build();

    private Color color2 = Color.builder()
            .id(2L)
            .type("red")
            .build();

    private Height height1 = Height.builder()
            .id(1L)
            .value(1.0)
            .build();

    private Height height2 = Height.builder()
            .id(2L)
            .value(7.0)
            .build();

    private Shape shape1 = Shape.builder()
            .id(1L)
            .type("circle")
            .build();

    private Shape shape2 = Shape.builder()
            .id(2L)
            .type("square")
            .build();

    private Wood wood1 = Wood.builder()
            .id(1L)
            .type("oak")
            .volumePrice(10.0)
            .build();

    private Wood wood2 = Wood.builder()
            .id(2L)
            .type("beech")
            .volumePrice(12.0)
            .build();

    private Paint paint1 = Paint.builder()
            .id(1L)
            .surfacePrice(0.9)
            .type("brillante")
            .build();

    private Paint paint2 = Paint.builder()
            .id(2L)
            .surfacePrice(0.3)
            .type("mate")
            .build();

    private Account account1 = Account.builder()
            .firstname("Jean")
            .id(1L)
            .lastname("Dupont")
            .mail("dupont@gmail.com")
            .password("245xxv69")
            .phoneNumber("02.01.03.26.89")
            .address("16 rue du pape")
            .build();

    private Account account11 = Account.builder()
            .firstname("Jean")
            .id(11L)
            .lastname("Dupont")
            .mail("dupont@gmail.com")
            .password("245xxv69")
            .address("20 rue du maire")
            .phoneNumber("05.01.03.29.89")
            .build();

    private Account updatedAccount1 = Account.builder()
            .firstname("Jean")
            .id(1L)
            .lastname("Dupont")
            .mail("UPDATED@gmail.com")
            .password("245xxv69")
            .address("16 rue du pape")
            .phoneNumber("06.01.03.86.89")
            .build();

    private Account account2 = Account.builder()
            .firstname("Gerard")
            .id(2L)
            .lastname("Dupont")
            .mail("dupont2@gmail.com")
            .password("xxxyyyzzz")
            .phoneNumber("02.78.03.26.95")
            .address("19 rue du dentiste")
            .build();


    /************************HELPER FUNCTIONS************************/
    private MockHttpServletResponse addAccount(Account account) throws Exception {
        String jsonInputAccount = this.jsonAccount.write(account).getJson();

        return mvc.perform(
                post("/accounts/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputAccount))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addColor(Color color) throws Exception {
        String jsonInputColor = this.jsonColor.write(color).getJson();
        return mvc.perform(
                post("/colors/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputColor))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addWood(Wood wood) throws Exception {
        String jsonInputWood = this.jsonWood.write(wood).getJson();
        return mvc.perform(
                post("/woods/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputWood))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addShape(Shape shape) throws Exception {
        String jsonInputShape = this.jsonShape.write(shape).getJson();
        return mvc.perform(
                post("/shapes/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputShape))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addPaint(Paint paint) throws Exception {
        String jsonInputPaint = this.jsonPaint.write(paint).getJson();
        return mvc.perform(
                post("/paints/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputPaint))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addBarrel(Barrel barrel) throws Exception {
        String jsonInputBarrel = this.jsonBarrel.write(barrel).getJson();

        return mvc.perform(
                post("/barrels/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputBarrel))
                .andReturn().getResponse();
    }

    private MockHttpServletResponse addHeight(Height height) throws Exception {
        String jsonInputHeight = this.jsonHeight.write(height).getJson();

        return mvc.perform(
                post("/heights/")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonInputHeight))
                .andReturn().getResponse();
    }

    /************************JACKSON************************/
    private JacksonTester<Account> jsonAccount; //JSON <-> Account translator
    private JacksonTester<Iterable<Account>> jsonListAccount; //JSON <-> Account List translator
    private JacksonTester<Barrel> jsonBarrel; //JSON <-> Barrel translator
    private JacksonTester<Color> jsonColor;
    private JacksonTester<Paint> jsonPaint;
    private JacksonTester<Wood> jsonWood;
    private JacksonTester<Shape> jsonShape;
    private JacksonTester<Height> jsonHeight;


    /************************SETUP************************/
    @Before
    public void setup() {
        //init ObjectMapper: JSON to Object
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JacksonTester.initFields(this, mapper);
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //************************CUSTOMER INTEGRATION TESTS************************//

    /************************CAS DROITS************************/
    @Test
    public void addAccountTest() throws Exception {
        //given account1
        MockHttpServletResponse postResponse = this.addAccount(account1);

        //then
        assertThat(postResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        //use jsonAccount to parse response, convert it to string (output string) and compare it to input string
        assertThat(this.jsonAccount.parseObject(postResponse.getContentAsString())).isEqualTo(account1);
    }

    @Test
    public void getAllAccountsTest() throws Exception {
        //given account1 & account2
        List accountList = List.of(account1, account2);

        this.addAccount(account1);
        this.addAccount(account2);


        MockHttpServletResponse getResponse;
        getResponse = mvc.perform(
                get("/accounts/list"))
                .andReturn().getResponse();

        //then
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        //use jsonAccount to parse response, convert it to string (output string) and compare it to input string
        assertThat(this.jsonListAccount.parseObject(getResponse.getContentAsString())).isEqualTo(accountList);
    }

    @Test
    public void updateAccountByIdTest() throws Exception {
        //add account1
        MockHttpServletResponse postResponse = this.addAccount(account1);
        assertThat(postResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(this.jsonAccount.parseObject(postResponse.getContentAsString())).isEqualTo(account1);

        //update account1 w/ updatedAccount1
        String jsonUpdatedAccount1 = this.jsonAccount.write(updatedAccount1).getJson();
        MockHttpServletResponse putResponse = mvc.perform(
                put("/accounts/1")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdatedAccount1))
                .andReturn().getResponse();

        //then
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        //use jsonAccount to parse response, convert it to string (output string) and compare it to input string
        assertThat(this.jsonAccount.parseObject(putResponse.getContentAsString())).isEqualTo(updatedAccount1);
    }

    @Test
    public void getAccountByMailTest() throws Exception {
        //given account1
        this.addAccount(account1);

        //when
        MockHttpServletResponse getResponse;
        getResponse = mvc.perform(
                get("/accounts/mail/" + account1.getMail())
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();
        //then
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        //use jsonAccount to parse response, convert it to string (output string) and compare it to input string
        assertThat(this.jsonAccount.parseObject(getResponse.getContentAsString())).isEqualTo(account1);
    }

    @Test
    public void deleteAccountTest() throws Exception {
        //given account1
        this.addAccount(account1);

        MockHttpServletResponse response = mvc.perform(
                delete("/accounts/" + account1.getId())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("Account has been deleted!");

    }


    /************************CAS LIMITES************************/
    @Test
    public void deleteNonExistingAccountTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(
                delete("/accounts/" + account1.getId())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void getAllAccountsEmptyListTest() throws Exception {
        MockHttpServletResponse getResponse;
        getResponse = mvc.perform(
                get("/accounts/list"))
                .andReturn().getResponse();

        //then
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    @Test
    public void updateNonExistingAccountTest() throws Exception {
        String jsonUpdatedAccount1 = this.jsonAccount.write(updatedAccount1).getJson();
        MockHttpServletResponse putResponse = mvc.perform(
                put("/accounts/1")
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdatedAccount1))
                .andReturn().getResponse();
        //then
        assertThat(putResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getNonExistingAccountTest() throws Exception {
        MockHttpServletResponse getResponse;
        getResponse = mvc.perform(
                get("/accounts/email/" + account1.getMail())
                        .accept(APPLICATION_JSON_UTF8)
                        .contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void emailConflictTest() throws Exception {
        //given account1 & account11 have the same email adress
        addAccount(account1);
        MockHttpServletResponse response = addAccount(account11);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }
    //************************CUSTOMER INTEGRATION TESTS************************//

    /************************CAS LIMITES************************/

    @Test
    public void createBarrelTest() throws Exception {
        this.addColor(color1);
        this.addColor(color2);
        this.addPaint(paint1);
        this.addPaint(paint2);
        this.addWood(wood1);
        this.addWood(wood2);
        this.addShape(shape1);
        this.addShape(shape2);
        this.addHeight(height1);
        this.addHeight(height2);


        //order book1
        MockHttpServletResponse postResponse1 = mvc.perform(
                post("/barrels/10")
                        .contentType(APPLICATION_JSON_UTF8))
                .andReturn().getResponse();

        assertThat(postResponse1.getStatus()).isEqualTo(HttpStatus.OK.value());

    }
}

