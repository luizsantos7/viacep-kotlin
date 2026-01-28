
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.CepEntity
import com.example.myapplication.data.model.CepResult
import com.example.myapplication.data.repository.CepLocalRepository
import com.example.myapplication.data.repository.CepRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CepViewModel(
    private val repository: CepRepository = CepRepository(),
    private val localRepository: CepLocalRepository = CepLocalRepository()
) : ViewModel() {
    private val _cepState = MutableStateFlow<CepState>(CepState.Initial)
    val cepState: StateFlow<CepState> = _cepState.asStateFlow()

    private val _listaFavoritos = MutableStateFlow<List<CepResult>>(emptyList())
    val listaFavoritos: StateFlow<List<CepResult>> = _listaFavoritos

    init {
        viewModelScope.launch { refreshFavsFromDb() }
    }

    fun buscarCep(cep: String) {
        viewModelScope.launch { buscarCepInterno(cep) }
    }

    private suspend fun buscarCepInterno(cep: String): Result<CepResult> {
        _cepState.value = CepState.Loading
        val resultado = repository.buscarCep(cep)
        _cepState.value = resultado.fold(
            onSuccess = { CepState.Success(cepResult = it) },
            onFailure = { CepState.Error(message = it.message ?: "Erro desconhecido") }
        )
        return resultado
    }

    fun favoritarCep(cep: String) {
        viewModelScope.launch {
            val resultado = buscarCepInterno(cep)
            resultado.onSuccess { cepResult ->
                val existente = localRepository.findByCep(cepResult.cep)
                if (existente == null) {
                    val entity = CepEntity(
                        cep = cepResult.cep,
                        logradouro = cepResult.logradouro,
                        complemento = cepResult.complemento,
                        bairro = cepResult.bairro,
                        estado = cepResult.estado,
                        uf = cepResult.uf,
                        isFav = true
                    )
                    localRepository.addFav(entity)
                    refreshFavsFromDb()
                }
            }
        }
    }

    fun desfavoritarCep(cep: String) {
        viewModelScope.launch {
            localRepository.removeFavByCep(cep)
            refreshFavsFromDb()
        }
    }

    private suspend fun refreshFavsFromDb() {
        val ceps = localRepository.getAll()
        _listaFavoritos.value = ceps.map { entity ->
            CepResult(
                cep = entity.cep,
                logradouro = entity.logradouro,
                complemento = entity.complemento,
                bairro = entity.bairro,
                estado = entity.estado,
                uf = entity.uf,
                erro = null,
                isFav = entity.isFav
            )
        }
    }

    sealed class CepState {
        object Initial : CepState()
        object Loading : CepState()
        data class Success(val cepResult: CepResult) : CepState()
        data class Error(val message: String) : CepState()
    }
}
