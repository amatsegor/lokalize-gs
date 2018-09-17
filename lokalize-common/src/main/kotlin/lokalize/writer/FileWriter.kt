package lokalize.writer

import lokalize.FileTools
import lokalize.Logger
import lokalize.models.LSEntity
import lokalize.models.Options
import lokalize.transformer.AbstractTransformer

class FileWriter : AbstractWriter() {
    override fun write(path: String, entities: List<LSEntity>, transformer: AbstractTransformer, options: Options) {
        var fileContent = ""

        val currentFolder = FileTools.getCurrentDirectory
        val filePath = "$currentFolder/$path"

        Logger.log("Writing file $filePath")

        if (FileTools.isFileExistsSync(filePath)) {
            fileContent = FileTools.readFileSync(path, options.encoding ?: "utf8")
        }

        val toInsert = transformer.transform(entities)
        val output = transformer.insert(fileContent, toInsert, options)

        writeFileAndCreateDirectoriesSync(filePath, output)
    }

    private fun writeFileAndCreateDirectoriesSync(filepath: String, content: String, encoding: String = "utf8") {
        val dirname = FileTools.getDirectories(filepath)
        FileTools.createPathSync(dirname)
        FileTools.writeFileSync(filepath, content, encoding)
    }
}