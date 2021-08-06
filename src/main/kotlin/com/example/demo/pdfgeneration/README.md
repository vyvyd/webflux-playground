# What 

This simple controller generates a PDF file and returns it in the server response 

# Why

This was a simple experiment to see why we are getting an error.

When the response  `Content-Type` header is `APPLICATION_PDF` the handler raises an `UnsupportedMediaTypeException`

```
org.springframework.web.reactive.function.UnsupportedMediaTypeException: Content type 'application/pdf' not supported for bodyType=org.springframework.web.reactive.function.BodyInserters$$Lambda$1067/0x00000008007cfc40
	at org.springframework.web.reactive.function.BodyInserters.unsupportedError(BodyInserters.java:391) ~[spring-webflux-5.3.9.jar:5.3.9]
	Suppressed: reactor.core.publisher.FluxOnAssembly$OnAssemblyException: 
```

This *might* have been caused due to using 

```kotlin
bodyValueAndAwait(BodyInserters.fromResource(resource))
```

instead of just 

```kotlin
bodyValueAndAwait(resource)
```

in com/example/demo/pdfgeneration/PdfGeneration.kt:53

# Test

Run the test `com.example.demo.DemoApplicationTests.canGeneratePdf`

The test should pass.
