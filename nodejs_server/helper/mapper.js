class mapper {
    static fromSPARQLToProp(elem) {
        return {
            name: elem.childLabel.value
        }
    }
}

module.exports = mapper;