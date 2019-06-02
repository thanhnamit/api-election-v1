Sample command to call Rule Engine API

```json
curl -X POST \
  http://localhost:8085/elections/2019/ballots \
  -H 'Accept: */*' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8085' \
  -d '{
	"ballot": {
		"enrolmentId": "123",
		"division": {
			"id": "D1",
			"suburbs": [
				{
					"postcode": "0000",
					"name": "Oz"
				}
			]
		},
		"houseOfRepBallot": {
			"votedCandidates": [
				{
					"voteNo": 1,
					"candidate": {
						"lastName": "Cat",
						"givenNames": "Meow",
						"party": {
							"name": "Mammals"
						}
					},
					"party": {
						"name": "Mammals"
					}
				},
				{
					"voteNo": 2,
					"candidate": {
						"lastName": "Dog",
						"givenNames": "Goof",
						"party": {
							"name": "Mammals"
						}
					},
					"party": {
						"name": "Mammals"
					}
				},
				{
					"voteNo": 3,
					"candidate": {
						"lastName": "Tona",
						"givenNames": "Goof",
						"party": {
							"name": "Fish"
						}
					},
					"party": {
						"name": "Fish"
					}
				}
			]
		},
		"senateBallot": {
			"voteType": "PARTY",
			"votedPartyCandidates": {
				"candidates": [
					{
						"voteNo": 1,
						"party": {
							"name": "Mammals"
						}
					}
				]
			}
		},
		"votedDate": "2019-01-01"
	},
	"witnessedBy": "test"
}'
```