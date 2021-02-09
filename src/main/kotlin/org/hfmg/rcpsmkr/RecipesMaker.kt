package org.hfmg.rcpsmkr

data class Ingrediente (
    val id: Int,
    val nombre: String,
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
    lateinit var ingredientes: MutableMap<Int, Ingrediente>
    lateinit var recetas: MutableMap<Int, Receta>
    var ultIDRec: Int = 0

    fun inicializarIngredientes() {
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

    fun inicializarRecetas() {
        recetas = mutableMapOf<Int, Receta>()
    }

    fun ejecutar() {
        inicializar()
        mostrarMenuPrincipal()
    }

    fun inicializar() {
        inicializarIngredientes()
        inicializarRecetas()
    }

    fun mostrarMensaje(mensaje: String) {
        print(mensaje)
        val toString = readLine().toString()
    }

    fun listarIngredientes() {
        for ((id, ingrediente) in ingredientes) {
            println("[${id}] - ${ingrediente.nombre}")
        }
    }

    fun listarIngredientesReceta(receta: Receta)  {
        var orden = 0
        for ((id, ingrec) in receta.ingredientes) {
            println("${++orden}. ${ingrec.ingrediente.nombre} (${ingrec.cantidad})")
        }
    }

    fun listarPasosReceta(receta: Receta) {
        for (paso in receta.preparacion) {
            println("- ${paso}.")
        }
    }

    fun listarRecetas() {
        for ((id, receta) in recetas) {
            println("[${id}] - ${receta.nombre}")
        }
    }

    fun mostrarRecetario() {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("RECETARIO:")
            listarRecetas()
            println("------------------------------------------------------")
            print("Elegir una receta [?] o salir con [Cero]: ")
            val opcion = readLine().toString()
            if (opcion == "0") {
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

    fun crearReceta() {
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
                else -> {
                    mostrarMensaje("Las opciones válidas son 1, 2, 3, 4 o Cero ... ")
                }
            }
        }

    }

    fun buscarReceta(nombre: String): Boolean {
        var encontrada = false
        for ((id, receta) in recetas) {
            encontrada = receta.nombre == nombre
            if (encontrada) break
        }
        return encontrada
    }

    fun ingresarNombre(receta: Receta): Receta {
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

    fun ingresarIngredientes(receta: Receta): Receta {
        var salir = false
        while (!salir) {
            println("\n------------------------------------------------------")
            println("INGREDIENTES DE LA RECETA:")
            listarIngredientesReceta(receta)
            println("------------------------------------------------------")
            println("INGREDIENTES DISPONIBLES:")
            listarIngredientes()
            println("------------------------------------------------------")
            print("Elegir un ingrediente [?] o salir con [Cero]: ")
            val opcion = readLine().toString()
            if (opcion == "0") {
                salir = true
            } else {
                val ingrediente = ingredientes[opcion.toInt()]
                if (ingrediente != null) {
                    print("Indicar la cantidad de [${ingrediente.nombre}]: ")
                    val cantidad = readLine().toString()
                    if (cantidad.isNotEmpty()) {
                        val ingredienteReceta = IngredienteReceta(ingrediente, cantidad)
                        receta.ingredientes[ingrediente.id] = ingredienteReceta
                    } else {
                        mostrarMensaje("Debe indicar la cantidad del ingrediente ... ")
                    }
                } else {
                    mostrarMensaje("Debe seleccionar codigo de receta valido ... ")
                }
            }
        }
        return receta
    }

    fun ingresarPreparacion(receta: Receta): Receta {
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

    fun verReceta(receta: Receta) {
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

    fun recetaValida(receta: Receta): Boolean {
        val nombreOk = !receta.nombre.isEmpty()
        val ingredientesOk = receta.ingredientes.isNotEmpty()
        val preparacionOk = !receta.preparacion.isNullOrEmpty()
        return (nombreOk && ingredientesOk && preparacionOk)
    }

    fun guardarReceta(receta: Receta): Boolean {
        var salir = false
        var guardada = false
        while (!salir) {
            print("Guarda la receta (Si={S/s}  o  No={N/n}: ")
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

    fun mostrarMenuPrincipal() {
        var opcion = ""
        while (opcion != "0") {
            println(
                """
                    ------------------------------------------------------
                    MENU PRINCIPAL
                    [1] - Hacer una receta
                    [2] - Ver mis recetas
                    [0] - Salir
                    ------------------------------------------------------
                """.trimIndent()
            )
            print("Elegir opcion: ")
            opcion = readLine().toString()
            when (opcion) {
                "1" -> crearReceta()
                "2" -> mostrarRecetario()
                "0" -> println("Opcion salir")
                else -> {
                    mostrarMensaje("Las opciones válidas son 1, 2 o Cero ... ")
                }
            }
        }
    }
}

fun main(args : Array<String>) {
    val rm = RecipesMaker()
    rm.ejecutar()
}