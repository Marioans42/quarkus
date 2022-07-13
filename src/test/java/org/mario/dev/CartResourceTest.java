package org.mario.dev;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.Test;
import org.mario.dev.util.TestContainerResource;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
@QuarkusTestResource(TestContainerResource.class)
public class CartResourceTest {
    private static final String INSERT_WRONG_CART_IN_DB =
            "insert into carts values (9999, current_timestamp, current_timestamp, 'NEW', 3)";

    private static final String DELETE_WRONG_CART_IN_DB =
            "delete from carts where id = 9999";

    @Inject
    public DataSource dataSource;

    @Test
    public void testGetActiveCartForCustomerWhenThereAreTwoCartsInDB() {
        executeSql(INSERT_WRONG_CART_IN_DB);
        given().when().
        get("/cart/customer/3").then()
                .statusCode(INTERNAL_SERVER_ERROR.code())
                .body(containsString(INTERNAL_SERVER_ERROR.reasonPhrase()))
                .body(containsString("Many active carts detected !!"));
        executeSql(DELETE_WRONG_CART_IN_DB);
    }

    private void executeSql(String query) {
        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new IllegalStateException("Error has occurred while trying to execute SQL Query " + e.getMessage());
        }
    }
}
