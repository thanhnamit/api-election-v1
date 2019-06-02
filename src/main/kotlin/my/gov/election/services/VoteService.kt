package my.gov.election.services

import my.gov.election.models.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.properties.Delegates

@Service
class VoteService(val ruleEngine: RuleEngine) {
    val logger: Logger = LoggerFactory.getLogger(VoteService::class.java)
    var parties: Map<String, Party> by Delegates.notNull()
    init {
        parties = mapOf<String, Party>(
                "mammals" to Party().name("Mammals"),
                "birds" to Party().name("Birds"),
                "fish" to Party().name("Fish"),
                "reptiles" to Party().name("Reptiles"),
                "amphibians" to Party().name("Amphibians")
        )
    }

    fun getBallot(postCode: String): Ballot {
        return Ballot()
                .houseOfRepBallot(getHouseOfRepBallot(postCode))
                .senateBallot(getSenateBallot())
                .division(Division().id("D1").suburbs(mutableListOf(Suburb().name("Australia").postcode(postCode))))
                .ballotVersion("v1.0")
    }

    private fun getSenateBallot(): SenateBallot? {
        return SenateBallot()
                .title("Senate Ballot")
                .individualCandidates(getIndividualCandidates())
                .partyCandidates(PartyCandidates().title("Above the line").candidates(
                        parties.values.toMutableList()
                ))
    }

    private fun getIndividualCandidates(): IndividualCandidates? {
        return IndividualCandidates()
                .title("Below the line")
                .candidates(getCandidatesByPostcode(""))
    }

    private fun getHouseOfRepBallot(postCode: String): HouseOfRepBallot? {
        return HouseOfRepBallot()
                .title("House of Representatives Ballot")
                .candidates(getCandidatesByPostcode(postCode))
    }

    private fun getCandidatesByPostcode(postCode: String): MutableList<Candidate>? {
        return mutableListOf(
                Candidate().givenNames("Cat").party(parties["mammals"]),
                Candidate().givenNames("Dogs").party(parties["mammals"]),
                Candidate().givenNames("Eagle").party(parties["birds"]),
                Candidate().givenNames("Tuna").party(parties["fish"]),
                Candidate().givenNames("Snake").party(parties["reptiles"]),
                Candidate().givenNames("Frog").party(parties["amphibians"])
        )
    }

    fun submitVote(vote: Vote): Vote {
        return vote.eligibility(ruleEngine.checkVote(vote, getBallot(vote.ballot.division.suburbs[0].postcode)))
    }
}