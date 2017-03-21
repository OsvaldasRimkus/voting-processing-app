function cleanSingleMandateVotingDataForChart(rawVotesData) {
    return rawVotesData.map(function (vote) {
        return {
            key: vote.candidate.firstName + ' ' + vote.candidate.lastName,
            value: vote.voteCount
        }
    })
}

function cleanMultiMandateVotingDataForChart(rawVotesData) {
    return rawVotesData.map(function (vote) {
        return {
            key: vote.party.name,
            value: vote.voteCount
        }
    })
}

function cleanConsolidatedResultsDataForChart(rawPartyResultsData) {
    return rawPartyResultsData.map(function (result) {
        return {
            key: result.party.name,
            value: result.mandates
        }
    })   
}


module.exports = {
    cleanSingleMandateVotingDataForChart, 
    cleanMultiMandateVotingDataForChart, 
    cleanConsolidatedResultsDataForChart
};