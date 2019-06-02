package my.gov.election.controllers

import my.gov.election.apis.VoteApi
import my.gov.election.models.Ballot
import my.gov.election.models.Vote
import my.gov.election.services.VoteService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class VoteController(val voteService: VoteService): VoteApi {
    override fun getBallot(postcode: String): ResponseEntity<Ballot> {
        return ResponseEntity.ok(voteService.getBallot(postcode))
    }

    override fun vote(body: Vote): ResponseEntity<Vote> {
        return ResponseEntity.ok(voteService.submitVote(body))
    }
}