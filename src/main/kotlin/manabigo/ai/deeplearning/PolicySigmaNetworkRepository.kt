package manabigo.ai.deeplearning

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.InputPreProcessor
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.Updater
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.conf.layers.setup.ConvolutionLayerSetup
import org.deeplearning4j.nn.conf.preprocessor.CnnToFeedForwardPreProcessor
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.DataInputStream
import java.io.DataOutputStream
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path

@Repository
class PolicySigmaNetworkRepository @Autowired constructor(
        @Value("modelDirectory") private val directoryPath: String
) {
    private val directory: Path
    private val charset = Charset.forName("UTF-8")
    private val lock = Object()

    init {
        this.directory = FileSystems.getDefault().getPath(directoryPath)
    }

    fun find(boardSize: Int): PolicySigmaNetwork {
        synchronized(lock) {
            val confFile = getConfFile(boardSize)
            val binFile = getBinFile(boardSize)
            if (Files.exists(confFile) && Files.exists(binFile)) {
                return load(boardSize, confFile, binFile)
            } else {
                return create(boardSize)
            }
        }
    }

    fun store(network: PolicySigmaNetwork): Unit {
        synchronized(lock) {
            val confFile = getConfFile(network.boardSize)
            val binFile = getBinFile(network.boardSize)
            val model = network.model
            Files.write(confFile, model.layerWiseConfigurations.toJson().toByteArray(charset))
            DataOutputStream(Files.newOutputStream(binFile)).use {
                Nd4j.write(model.params(), it)
            }
        }
    }

    private fun getConfFile(boardSize: Int): Path {
        return directory.resolve("policy_sigma_network_$boardSize.json")
    }

    private fun getBinFile(boardSize: Int): Path {
        return directory.resolve("policy_sigma_network_$boardSize.bin")
    }

    private fun load(boardSize: Int, confFile: Path, binFile: Path): PolicySigmaNetwork {
        val conf: MultiLayerConfiguration = MultiLayerConfiguration.fromJson(
                String(Files.readAllBytes(confFile), charset)
        )
        val params: INDArray = DataInputStream(Files.newInputStream(binFile)).use {
            Nd4j.read(it)
        }
        val model = MultiLayerNetwork(conf)
        model.init()
        model.setParameters(params)
        return PolicySigmaNetwork(model, boardSize)
    }

    private fun create(boardSize: Int): PolicySigmaNetwork {
        val filterNum = 20

        val builder = NeuralNetConfiguration.Builder()
                .seed(12345)
                .iterations(1)
                .regularization(true).l2(0.0005)
                .useDropConnect(true)
                .learningRate(0.01)
                .weightInit(WeightInit.XAVIER)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS).momentum(0.9)
                .list(12)
        builder.layer(0, ConvolutionLayer.Builder(5, 5)
                .padding(2, 2)
                .stride(1, 1)
                .nOut(filterNum)
                .activation("relu")
                .build())
        (1..10).forEach { n ->
            builder.layer(n, ConvolutionLayer.Builder(3, 3)
                    .padding(1, 1)
                    .stride(1, 1)
                    .nOut(filterNum)
                    .activation("relu")
                    .build())
        }

        builder.layer(11, OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(boardSize * boardSize)
                .activation("softmax")
                .build())
                .backprop(true).pretrain(false);
        ConvolutionLayerSetup(builder, boardSize, boardSize, 3)
        val conf: MultiLayerConfiguration = builder.build()
        val preprocessors: Map<Int, InputPreProcessor> = mapOf(
                0 to conf.getInputPreProcess(0),
                11 to CnnToFeedForwardPreProcessor(boardSize, boardSize, filterNum)
        )
        conf.inputPreProcessors = preprocessors
        val model = MultiLayerNetwork(conf)
        model.init()

        return PolicySigmaNetwork(model, boardSize)
    }
}
