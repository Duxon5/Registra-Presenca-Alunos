package br.com.registradorpresencaalunos.repository

data class Disciplina(
    val idDisciplina: Int,
    val idUsuario: String,
    val inicioAula: String,
    val fimAula: String,
    val diaSemana: String,
    val materia: String,
    val professor: String,
    val localAula: String,
    var presencaRegistrada: Boolean,
    val localLatitude: String,
    val localLongitude: String,
    var localRegistro: String
)

data class Usuario(
    val usuario: String,
    val senha: String
)

class Dados {

    private val mListUsuario: List<Usuario> = listOf(
        Usuario(
            "123","1"
        ),
        Usuario(
            "456","2"
        ),
        Usuario(
            "789","3"
        )
    )

    fun validaLoginSenha(usuario: String, senha: String): Boolean = mListUsuario.filter {it.usuario == usuario && it.senha == senha}.any()

    private val mListDisciplina: List<Disciplina> = listOf(
        Disciplina(
            1,"123","19:10:00","21:50:00","segunda-feira","LINGUAGENS FORMAIS E AUTÔMATOS","Juliano Ratuznei","Bloco ALFA, Sala 115, Andar 1",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            2,"123","19:10:00","21:50:00","terça-feira","TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I","Tatiana","Bloco ALFA, Sala 407, Andar 4",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            3,"123","19:10:00","21:50:00","quarta-feira","PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS","Alexandre Rangel","Bloco A, Sala LAB.INF.5, Andar TÉRREO",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            4,"123","EAD","EAD","EAD","INTELIGÊNCIA ARTIFICIAL (EAD)","Rafael","EAD",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            1,"456","19:10:00","21:50:00","segunda-feira","LINGUAGENS FORMAIS E AUTÔMATOS","Juliano Ratuznei","Bloco ALFA, Sala 115, Andar 1",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            2,"456","19:10:00","21:50:00","terça-feira","TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I","Tatiana","Bloco ALFA, Sala 407, Andar 4",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            3,"456","19:10:00","21:50:00","quarta-feira","PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS","Alexandre Rangel","Bloco A, Sala LAB.INF.5, Andar TÉRREO",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            4,"456","EAD","EAD","EAD","INTELIGÊNCIA ARTIFICIAL (EAD)","Rafael","EAD",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            1,"789","19:10:00","21:50:00","segunda-feira","LINGUAGENS FORMAIS E AUTÔMATOS","Juliano Ratuznei","Bloco ALFA, Sala 115, Andar 1",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            2,"789","19:10:00","21:50:00","terça-feira","TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I","Tatiana","Bloco ALFA, Sala 407, Andar 4",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            3,"789","19:10:00","21:50:00","quarta-feira","PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS","Alexandre Rangel","Bloco A, Sala LAB.INF.5, Andar TÉRREO",false,"-23.536286105990403","-46.560337171952156",""
        ),
        Disciplina(
            4,"789","EAD","EAD","EAD","INTELIGÊNCIA ARTIFICIAL (EAD)","Rafael","EAD",false,"-23.536286105990403","-46.560337171952156",""
        )
    )

    private fun removeCharInicioFim(nome: String): String{
        val nomeTam = nome.length
        return nome.substring(1, nomeTam - 1)
    }

    fun getTodasMaterias(idUsuario: String): List<String> = mListDisciplina.filter{it.idUsuario == idUsuario}.map{ it.materia }
//    fun getTodasMaterias(): List<String> = mListDisciplina.map { it.materia }

    fun getIdPelaMateria(nomeMateria: String, idUsuario: String): Int {
        var valorEncontrado = mListDisciplina.find { it.materia == nomeMateria  && it.idUsuario == idUsuario}
        var idDisciplina: Int = -1
        if (valorEncontrado != null) {
            idDisciplina = valorEncontrado.idDisciplina
        }
        return idDisciplina
    }

    fun getIdPeloDiaSemana(diaSemana: String, idUsuario: String): Int{
        var valorEncontrado = mListDisciplina.find{it.diaSemana == diaSemana && it.idUsuario == idUsuario}
        var idDisciplina: Int = -1
        if (valorEncontrado != null){
            idDisciplina = valorEncontrado.idDisciplina
        }
        return idDisciplina
    }

    fun getMateriaPeloDiaSemana(diaSemana: String, idUsuario: String) = mListDisciplina.filter{it.diaSemana == diaSemana && it.idUsuario == idUsuario}.map{it.materia}

    fun hasMateriaHoje(diaSemana: String, idUsuario: String): Boolean = mListDisciplina.filter {it.diaSemana == diaSemana && it.idUsuario == idUsuario}.any()

    fun getInicioAula(idDisciplina: Int, idUsuario: String): String{
        val inicioAulaSelecionada = mListDisciplina.filter{ (it.idDisciplina == idDisciplina && it.idUsuario == idUsuario)}.map{it.inicioAula}

        return removeCharInicioFim(inicioAulaSelecionada.toString())
    }

    fun getFimAula(idDisciplina: Int, idUsuario: String): String{
        val fimAulaSelecionada = mListDisciplina.filter{ (it.idDisciplina == idDisciplina && it.idUsuario == idUsuario)}.map{it.fimAula}

        return removeCharInicioFim(fimAulaSelecionada.toString())
    }

    fun getDiaSemana(idDisciplina: Int, idUsuario: String): String{
        val diaSemana = mListDisciplina.filter{ (it.idDisciplina == idDisciplina && it.idUsuario == idUsuario)}.map{it.diaSemana}

        return removeCharInicioFim(diaSemana.toString())
    }

    fun getMateria(idDisciplina: Int, idUsuario: String): String{
        val materiaSelecionada = mListDisciplina.filter{ (it.idDisciplina == idDisciplina  && it.idUsuario == idUsuario)}.map{it.materia}

        return removeCharInicioFim(materiaSelecionada.toString())
    }

    fun getProfessor(idDisciplina: Int, idUsuario: String): String{
        val professorSelecionado = mListDisciplina.filter{ (it.idDisciplina == idDisciplina && it.idUsuario == idUsuario)}.map{it.professor}

        return removeCharInicioFim(professorSelecionado.toString())
    }

    fun getLocalAula(idDisciplina: Int, idUsuario: String): String{
        val localAulaSelecionada = mListDisciplina.filter{ (it.idDisciplina == idDisciplina  && it.idUsuario == idUsuario)}.map{it.localAula}

        return removeCharInicioFim(localAulaSelecionada.toString())
    }

    fun getPresencaRegistrada(idDisciplina: Int, idUsuario: String): String {
        var valorEncontrado = mListDisciplina.find{it.idDisciplina == idDisciplina && it.idUsuario == idUsuario}
        var presencaRegistrada: Boolean = false
        if (valorEncontrado != null){
            presencaRegistrada = valorEncontrado.presencaRegistrada
        }
        if(presencaRegistrada == true){ return "SIM" }
        else{ return "NÃO" }

    }

    fun setPresencaRegistrada(idDisciplina: Int, idUsuario: String): Boolean? {
        var valorEncontrado = mListDisciplina.find{it.idDisciplina == idDisciplina && it.idUsuario == idUsuario}
        valorEncontrado?.presencaRegistrada = true
        return valorEncontrado?.presencaRegistrada
    }

    fun getLocalRegistro(idDisciplina: Int, idUsuario: String): String{
        val localRegistroSelecionado = mListDisciplina.filter{ (it.idDisciplina == idDisciplina  && it.idUsuario == idUsuario)}.map{it.localRegistro}

        return removeCharInicioFim(localRegistroSelecionado.toString())
    }

    fun setLocalRegistro(idDisciplina: Int, idUsuario: String, localLatLong: String): String? {
        var valorEncontrado = mListDisciplina.find{it.idDisciplina == idDisciplina && it.idUsuario == idUsuario}
        valorEncontrado?.localRegistro = localLatLong
        return valorEncontrado?.localRegistro
    }

}