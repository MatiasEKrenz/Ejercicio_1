## Primera API RESTful

#Uso
* Crear un usuario: Método POST en /usuario. 
  Cuerpo: int id; String nombre; String apellido;
  
* Mostrar todos los usuarios: Método GET en /usuario.

* Mostrar un usuario por id: Método GET en /usuario/:id.

* Modificar un usuario existente: Método PUT en /usuario.
  Cuerpo: int id; String nombre; String apellido;
  
* Eliminar un usuario: Método DELETE en /usuario/:id.
