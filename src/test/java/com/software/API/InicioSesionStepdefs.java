package com.software.API;

import com.software.API.modelo.Usuario;
import com.software.API.servicio.ServicioInicioSesion;
import com.software.API.servicio.impl.ServicioInicioSesionImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InicioSesionStepdefs {

    private final ServicioInicioSesion servicioInicioSesion;
    private String resultado;
    private Usuario usuario;

    public InicioSesionStepdefs() {
        servicioInicioSesion = new ServicioInicioSesionImpl();
    }

    @Given("el usuario tiene un correo {string} y una contrasenia {string}")
    public void elUsuarioTieneUnCorreoYUnaContrasenia(String email, String contrasenia) {
        usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasenia(contrasenia);
    }

    @When("intenta iniciar sesion con correo {string} y contrasenia {string}")
    public void intentaIniciarSesionConCorreoYContrasenia(String email, String contrasenia) {
        Usuario usuarioInicioSesion = new Usuario();
        usuarioInicioSesion.setEmail(email);
        usuarioInicioSesion.setContrasenia(contrasenia);

        resultado = servicioInicioSesion.inicioSesion(usuario, usuarioInicioSesion);
    }

    @Then("el sistema debe permitir el acceso")
    public void elSistemaDebePermitirElAcceso() {
        assertEquals("Inicio de sesión exitoso.", resultado);
    }

    @Then("el sistema debe mostrar un mensaje de error {string}")
    public void elSistemaDebeMostrarUnMensajeDeError(String mensaje) {
        assertEquals(mensaje, resultado);
    }
}
