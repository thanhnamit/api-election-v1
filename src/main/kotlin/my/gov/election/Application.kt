package my.gov.election

import org.kie.api.KieServices
import org.kie.api.runtime.KieContainer
import org.kie.internal.io.ResourceFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application {
	@Bean
	fun kieContainer(): KieContainer {
		val svc = KieServices.Factory.get()
		val fs = svc.newKieFileSystem()
		fs.write(ResourceFactory.newClassPathResource("rules/vote-eligibility.drl"))
		svc.newKieBuilder(fs).buildAll()
		return svc.newKieContainer(svc.newKieBuilder(fs).kieModule.releaseId)
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
