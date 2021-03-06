package org.hfmg.rcpsmkr

data class Ingrediente (
    val id: Int,
    var nombre: String,
)

data class IngredienteReceta (
    val ingrediente: Ingrediente,
    val cantidad: String
)

data class Receta(
    var nombre: String,
    val ingredientes: MutableMap<Int, IngredienteReceta>,
    var preparacion: MutableList<String>
)

class RecipesMaker {
    private lateinit var ingredientes: MutableMap<Int, Ingrediente>
    private lateinit var recetas: MutableMap<Int, Receta>
    private var ultIDRec: Int = 0

    fun ejecutar() {
        inicializar()
        mostrarMenuPrincipal()
    }

    private fun inicializar() {
        inicializarIngredientes()
        inicializarRecetas()
    }

    private fun inicializarIngredientes() {
        ingredientes = mutableMapOf<Int, Ingrediente>()
        ingredientes[1] = Ingrediente(1, "Agua")
        ingredientes[2] = Ingrediente(2, "Leche")
        ingredientes[3] = Ingrediente(3, "Carne")
        ingredientes[4] = Ingrediente(4, "Verduras")
        ingredientes[5] = Ingrediente(5, "Frutas")
        ingredientes[6] = Ingrediente(6, "Cereal")
        ingredientes[7] = Ingrediente(7, "Huevos")
        ingredientes[8] = Ingrediente(8, "Aceite")
    }

    private fun inicializarRecetas() {
        recetas = mutableMapOf<Int, Receta>()
    }

    private fun listarIngredientes() {
        for ((id, ingrediente) in ingredientes) {
            println("[${id}] - ${ingrediente.nombre}")
        }
    }

    private fun listarIngredientesReceta(receta: Receta)  {
        var orden = 0
        for ((id, ingrec) in receta.ingredientes) {
            println("${++orden}. ${ingrec.ingrediente.nombre} (${ingrec.cantidad})")
        }
    }

    private fun listarPasosReceta(receta: Receta) {
        for (paso in receta.preparacion) {
            println("- ${paso}.")
        }
    }

    private fun listarRecetas() {
        for ((id, receta) in recetas) {
            println("[${id}] - ${receta.nombre}")
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        print(mensaje)
        val toString = readLine().toString()
    }

    private fun mostrarRecetario() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("RECETARIO:")
            listarRecetas()
            println("------------------------------------------------------")
            print("Elegir una codigo de receta [?] o [S] para retornar: ")
            val opcion = readLine().toString()
            if (opcion == "S" || opcion == "s") {
                salir = true
            } else {
                if (opcion != "") {
                    try {
                        val id = opcion.toInt()
                        val receta = recetas[id]
                        if (receta != null) {
                            verReceta(receta)
                        } else {
                            mostrarMensaje("Debe seleccionar codigo de receta valido ... ")
                        }
                    } catch (exception: Exception) {
                        mostrarMensaje("Debe seleccionar codigo de receta valido ... ")
                    }
                } else {
                    mostrarMensaje("Debe seleccionar codigo de receta valido ... ")
                }
            }
        }
    }

    private fun mostrarIngredientes() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES:")
            listarIngredientes()
            println("------------------------------------------------------")
            print("Elegir operacion [C]rear, [M]odificar, [E]liminar o [S]alir: ")
            val opcion = readLine().toString()

            when (opcion) {
                "C", "c" -> crearIngrediente()
                "M", "m" -> modificarIngrediente()
                "E", "e" -> eliminarIngrediente()
                "S", "s" -> salir = true
                else -> mostrarMensaje("Las opciones válidas son C, M, E, o S ... ")
            }
        }
    }

    private fun obtenerKeyMayor(keys: MutableSet<Int>): Int {
        var mayor = 0
        if (!keys.isEmpty()) {
            for (key in keys) {
                if (key > mayor) {
                    mayor = key
                }
            }
        }
        return mayor
    }

    private fun crearIngrediente() {
        println("------------------------------------------------------")
        print("Ingresar el nombre del nuevo ingrediente: ")
        val nombre = readLine().toString()
        if (nombre != "") {
            val key = obtenerKeyMayor(ingredientes.keys)
            ingredientes[key+1] = Ingrediente(key+1, nombre)
            mostrarMensaje("Se creo el nuevo ingrediente ... ")
        } else {
            mostrarMensaje("No ingreso ningun nombre ... ")
        }
    }

    private fun modificarIngrediente() {3
        println("------------------------------------------------------")
        print("Ingresar el codigo del ingrediente a modificar: ")
        val codigo = readLine().toString()
        try {
            val ingrediente = ingredientes[codigo.toInt()]
            print("Cambiar [${ingrediente!!.nombre}] por: ")
            val nombre = readLine().toString()
            if (nombre != "") {
                ingrediente?.nombre = nombre
                ingredientes[codigo.toInt()] = ingrediente
                mostrarMensaje("Se modifico el ingrediente ... ")
            } else {
                mostrarMensaje("No ingreso ningun nombre ... ")
            }
        } catch (exception: Exception) {
            mostrarMensaje("Debe seleccionar un codigo de ingrediente valido ... ")
        }
    }

    private fun eliminarIngrediente() {
        println("------------------------------------------------------")
        print("Ingresar el codigo del ingrediente ha eliminar: ")
        val codigo = readLine().toString()
        try {
            val key = codigo.toInt()
            val ingrediente = ingredientes[key]
            if (ingredientes.remove(key, ingrediente!!)) {
                mostrarMensaje("Se elimino el ingrediente ... ")
            } else {
                mostrarMensaje("No se pudo elimnar el ingrediente ... ")
            }
        } catch (exception: Exception) {
            mostrarMensaje("Debe ingresar un codigo de ingrediente valido ... ")
        }
    }

    private fun crearReceta() {
        val receta = Receta("", mutableMapOf(), mutableListOf())
        var salir = false
        while (!salir) {
            println(
                """
                    ------------------------------------------------------
                    CREANDO NUEVA RECETA
                    [1] - Definir nombre
                    [2] - Ingresar ingredientes
                    [3] - Describir preparacion
                    [4] - Mostrar receta
                    [0] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elegir opcion: ")
            val opcion = readLine().toString()
            when (opcion) {
                "1" -> ingresarNombre(receta)
                "2" -> ingresarIngredientes(receta)
                "3" -> ingresarPreparacion(receta)
                "4" -> verReceta(receta)
                "0" -> salir = guardarReceta(receta)
                else -> mostrarMensaje("Las opciones válidas son 1, 2, 3, 4 o Cero ... ")
            }
        }

    }

    private fun buscarReceta(nombre: String): Boolean {
        var encontrada = false
        for ((id, receta) in recetas) {
            encontrada = receta.nombre == nombre
            if (encontrada) break
        }
        return encontrada
    }

    private fun ingresarNombre(receta: Receta): Receta {
        print("Ingrese el nombre de la receta: ")
        val nombre = readLine().toString()
        if (nombre.isEmpty()) {
            println("Ingreso un nombre vacio")
            return receta
        } else {
            if (buscarReceta(nombre)) {
                println("Ingreso un nombre ya registrado")
                return receta
            } else {
                println("Nombre Registrado")
                receta.nombre = nombre
                return receta
            }
        }
    }

    private fun ingresarIngredientes(receta: Receta): Receta {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES DE LA RECETA:")
            listarIngredientesReceta(receta)
            println("------------------------------------------------------")
            println("INGREDIENTES DISPONIBLES:")
            listarIngredientes()
            println("------------------------------------------------------")
            print("Elegir un codigo de ingrediente [?] o salir con [S]: ")
            val opcion = readLine().toString()
            if (opcion == "S" || opcion == "s") {
                salir = true
            } else {
                if (opcion != "") {
                    try {
                        val ingrediente = ingredientes[opcion.toInt()]
                        if (ingrediente != null) {
                            var cantidad = ""
                            do {
                                print("Indicar la cantidad de [${ingrediente.nombre}]: ")
                                cantidad = readLine().toString()
                                if (cantidad.isNotEmpty()) {
                                    val ingredienteReceta = IngredienteReceta(ingrediente, cantidad)
                                    receta.ingredientes[ingrediente.id] = ingredienteReceta
                                } else {
                                    mostrarMensaje("Debe indicar una cantidad de [${ingrediente.nombre}] valida ... ")
                                }
                            } while (cantidad == "")
                        } else {
                            mostrarMensaje("Debe seleccionar un codigo de ingrediente valido ... ")
                        }
                    } catch (exception: Exception) {
                        mostrarMensaje("Debe seleccionar un codigo de ingrediente valido ... ")
                    }
                } else {
                    mostrarMensaje("Debe seleccionar un codigo de ingrediente valido ... ")
                }
            }
        }
        return receta
    }

    private fun ingresarPreparacion(receta: Receta): Receta {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("PASOS PARA PREPARAR LA RECETA:")
            listarPasosReceta(receta)
            println("------------------------------------------------------")
            println("Ingresar una accion para la elaboracion de la receta (Cero terminar): ")
            val paso = readLine().toString()
            if (paso.isNotEmpty()) {
                if (paso != "0") {
                    receta.preparacion.add(paso)
                } else {
                    salir = true
                }
            } else {
                mostrarMensaje("Debe ingresar una paso no vacio ... ")
            }
        }
        return receta
    }

    private fun verReceta(receta: Receta) {
        println("..................................................................................")
        println("RECETA:")
        println('"' + receta.nombre + '"')
        println("INGREDIENTES:")
        listarIngredientesReceta(receta)
        println("PREPARACION:")
        listarPasosReceta(receta)
        println("..................................................................................")
        mostrarMensaje("Precione cualquier tecla para retornar ... ")
    }

    private fun recetaValida(receta: Receta): Boolean {
        val nombreOk = !receta.nombre.isEmpty()
        val ingredientesOk = receta.ingredientes.isNotEmpty()
        val preparacionOk = !receta.preparacion.isNullOrEmpty()
        return (nombreOk && ingredientesOk && preparacionOk)
    }

    fun guardarReceta(receta: Receta): Boolean {
        var salir = false
        var guardada = false
        while (!salir) {
            print("Usted guarda la receta (Si={S/s}  o  No={N/n}: ")
            val opcion = readLine().toString()
            when (opcion) {
                "N","n" -> salir = true
                "S","s" -> {
                    if (recetaValida(receta)) {
                        recetas[++ultIDRec] = receta
                        guardada = true
                    } else {
                        mostrarMensaje("Receta incompleta, verificar ... ")
                    }
                    salir = true
                }
                else -> {
                    mostrarMensaje("Las opciones válidas son {S/s}, {N/n} o Cero ... ")
                }
            }
        }
        return guardada
    }

    private fun mostrarMenuPrincipal() {
        var opcion = ""
        while (opcion != "S" && opcion != "s") {
            println(
                """
                    ------------------------------------------------------
                    MENU PRINCIPAL
                    [1] - Hacer una receta
                    [2] - Ver mis recetas
                    [3] - Ver mis ingredientes
                    [S] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elegir opcion: ")
            opcion = readLine().toString()
            when (opcion) {
                "1" -> crearReceta()
                "2" -> mostrarRecetario()
                "3" -> mostrarIngredientes()
                "S", "s" -> mostrarMensaje("*** Hasta pronto *** ... ")
                else -> {
                    mostrarMensaje("Las opciones válidas son 1, 2, 3 o S ... ")
                }
            }
        }
    }
}

fun main(args : Array<String>) {
    val rm = RecipesMaker()
    rm.ejecutar()
}