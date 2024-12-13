Feature: Servicio de inicio de sesión
  Como usuario del sistema
  Quiero poder iniciar sesión con mis credenciales
  Para acceder a las funcionalidades protegidas del sistema

  Background:
    Given el usuario tiene un correo "usuario@dominio.com" y una contrasenia "123456"

  Scenario: Inicio de sesión exitoso con credenciales válidas
    When intenta iniciar sesion con correo "usuario@dominio.com" y contrasenia "123456"
    Then el sistema debe permitir el acceso

  Scenario: Inicio de sesión fallido por usuario incorrecto
    When intenta iniciar sesion con correo "incorrecto@dominio.com" y contrasenia "123456"
    Then el sistema debe mostrar un mensaje de error "Usuario incorrecto."

  Scenario: Inicio de sesión fallido por contraseña incorrecta
    When intenta iniciar sesion con correo "usuario@dominio.com" y contrasenia "abcd"
    Then el sistema debe mostrar un mensaje de error "Contraseña incorrecta."

