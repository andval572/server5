package com.pokemon.server

import org.springframework.web.bind.annotation.*

@RestController
class UsuarioController(private val usuarioRepository: UsuarioRepository) {

    @GetMapping("crearUsuario/{nombre}/{pass}")
    @Synchronized
    fun requestCrearUsuario(@PathVariable nombre: String, @PathVariable pass: String): Any {
        val userOptinal = usuarioRepository.findById(nombre)

        return if (userOptinal.isPresent) {
            val user = userOptinal.get()
            if (user.pass == pass) {
                user
            } else {
                "Contraseña incorrecta"
            }
        } else {
            val user = Usuario(nombre, pass)
            usuarioRepository.save(user)
            user
        }
    }

    /*
    curl --request POST  --header "Content-type:application/json" --data "{\"nombre\":\"u2\", \"pass\":\"p2\"}" localhost:8084/crearUsuario {"nombre":"u2","pass":"p2","token":"u2p2"}
     */
    @PostMapping("crearUsuario")
    @Synchronized
    fun requestCrearUsuarioJson(@RequestBody usuario: Usuario): Any {
        val userOptional = usuarioRepository.findById(usuario.nombre)

        return if (userOptional.isPresent) {
            val user = userOptional.get()
            if (user.pass == usuario.pass) {
                user
            } else {
                "Contraseña incorrecta"
            }
        } else {
            usuarioRepository.save(usuario)
            usuario
        }
    }

    @PostMapping("pokemonFavorito/{token}/{pokemonId}")
    fun requestPokemonFavorito(@PathVariable token: String, @PathVariable pokemonId: Int): Any {
        usuarioRepository.findAll().forEach { user ->
            if (user.token == token) {
                user.pokemonFavoritoId = pokemonId
                usuarioRepository.save(user)
                return "El USUARIO ${user.nombre} tiene un nuevo pokemon favorito"
            }
        }
        return "Token no encontrado"
    }
}