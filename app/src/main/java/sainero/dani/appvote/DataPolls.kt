package sainero.dani.appvote

data class DataPolls(
    val opciones: MutableList<String>,
    val question: String,
    val respuestas: MutableList<String>,
    val usuarios: MutableList<String>,
    val id: String
)