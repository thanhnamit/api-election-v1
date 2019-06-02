package my.gov.election.services

import my.gov.election.models.Ballot
import my.gov.election.models.Vote
import my.gov.election.models.VoteEligibility
import org.drools.core.event.DebugAgendaEventListener
import org.drools.core.event.DebugRuleRuntimeEventListener
import org.kie.api.runtime.KieContainer
import org.springframework.stereotype.Service

@Service
class RuleEngine(val kieContainer: KieContainer) {
    fun checkVote(vote: Vote, originalBallot: Ballot): VoteEligibility {
        var eligibility = VoteEligibility().status(false).reason("Invalid Vote")
        val sess = kieContainer.newKieSession()
        sess.addEventListener(DebugRuleRuntimeEventListener())
        sess.addEventListener(DebugAgendaEventListener())
        sess.setGlobal("eligibility", eligibility)
        sess.insert(vote)
        sess.insert(originalBallot)
        sess.fireAllRules()
        sess.dispose()
        return eligibility
    }
}