package com.example.demo.pdfgeneration

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfName
import com.lowagie.text.pdf.PdfString
import com.lowagie.text.pdf.PdfWriter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class PdfFile(
    private val name: String
) {
    fun asOutputStream(): ByteArrayOutputStream {
        val document = Document()
        val outputStream = ByteArrayOutputStream()
        val instance: PdfWriter = PdfWriter.getInstance(
            document,
            outputStream
        )
        document.open()
        instance.info.put(PdfName.CREATOR, PdfString(Document.getVersion()))
        document.add(Paragraph("Hello $name. this is your PDF"))
        document.close()
        return outputStream
    }
}


@Component
class PdfGenerationHandler {
    suspend fun generatePdf(request: ServerRequest): ServerResponse {
        val pdfOutputStream = PdfFile(request.pathVariable("name")).asOutputStream()
        val resource = InputStreamResource(
            ByteArrayInputStream(pdfOutputStream.toByteArray())
        )
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_PDF)
            .bodyValueAndAwait(resource)
    }
}

@Configuration(proxyBeanMethods = false)
class PdfGenerationRouter(
    private val pdfGenerationHandler: PdfGenerationHandler
) {

    @Bean
    @FlowPreview
    fun postNewPdf() = coRouter {
        GET("/newpdf/{name}", pdfGenerationHandler::generatePdf)
    }

}