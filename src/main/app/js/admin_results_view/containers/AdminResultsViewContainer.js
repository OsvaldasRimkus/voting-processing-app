var React = require('react');
var axios = require('axios');
var AdminResultsViewComponent = require('../components/AdminResultsViewComponent');
var CountyDisplayContainer = require('./CountyDisplayContainer');
var spring = require('../../config/SpringConfig');

var timer = null;

var AdminResultsViewContainer = React.createClass({
	getInitialState: function() {
		return ({ optionsMap: new Map(),
				  counties: [],
				  activeDistrict: 0,
				  activeCounty: 0,
        	      query: ''
		});
	},
	componentDidMount: function() {
		var _this = this;

		axios.get(spring.localHost.concat('/api/county'))
			.then(function(response) {
				
				var counties = response.data;
				var optionsMap = new Map();

				counties.forEach(county => {
					var district = county.district.name
					if (optionsMap.get(district)) {
						optionsMap.get(district).push(county)
					} else {
						optionsMap.set(district, [county])
					}
				});

				_this.setState({ optionsMap: optionsMap,
								 counties: counties });
			})
			.catch(function(err) {
				console.log(err);
			})

	},
	setActiveDistrict: function(e) {
		this.setState({ activeDistrict: e.target.value, activeCounty: 0, query: '' });
	},
	setActiveCounty: function(e) {
		this.setState({ activeCounty: e.target.value, query: '' });
	},
	filterByCounty(county) {
		return county.name === this.state.activeCounty
	},
	filterByDistrict(county) {
		return county.district.name === this.state.activeDistrict
	},
    filterByQuery(county) {
		return county.name.toLowerCase().includes(this.state.query) ||
			county.district.name.toLowerCase().includes(this.state.query);
    },
    onKeyUp() {
        const value = document.getElementById("territorial-search").value;
        const _this = this;

        $('#territorial-search').keyup(function() {
            clearTimeout(timer);
            timer = setTimeout(_this.handleChangeQuery.bind(_this, value), 1000);
        })
    },
    handleChangeQuery: function(value) {
        this.setState({ query: value.toLowerCase(), activeDistrict: 0, activeCounty: 0 });
    },
    clearQuery: function() {
        this.setState({ query: '' });
    },
	prepareCounties() {
		let counties = this.state.counties;

		if (this.state.activeCounty != 0) {
			counties = counties.filter(this.filterByDistrict).filter(this.filterByCounty)
		} else if (this.state.activeDistrict != 0) {
			counties = counties.filter(this.filterByDistrict)
		} else {
			counties = counties.filter(this.filterByQuery);
		}

		return counties.map((c, idx) => {
			return (
				<CountyDisplayContainer
					key={c.district.id + "." + c.id}
					index={idx}
					unit={c}
				/>
			)
		});
	},
	districtsSelect: function() {
		var districtOptions = [];

		this.state.optionsMap.forEach((v, k) => {
			districtOptions.push(
				<option
					value={k}
					key={k + "-" + v.name}
				> 
					{k} 
				</option>
			);			
		});

		return (
			<select value={this.state.activeDistrict} onChange={this.setActiveDistrict}>
				<option
					value={0}
					key={districtOptions.length}
				>
					Visos apygardos
				</option>
				{districtOptions}
			</select>
		);
	},
	countiesSelect: function() {
		var countyOptions = [];
		var dict = this.state.optionsMap;
		var activeDistrict = this.state.activeDistrict;

		if (activeDistrict == 0) return undefined;

		dict.get(activeDistrict).forEach(c => {
			countyOptions.push(
				<option
					value={c.name}
					key={activeDistrict + '-' + c.name}
				>
					{c.name}
				</option>
			);
		});

		return (
			<select value={this.state.activeCounty} onChange={this.setActiveCounty}>
				<option
					value={0}
					key={countyOptions.length}
				>
					Visos apylinkės
				</option>
				{countyOptions}
			</select>
		);
	},
	render: function() {
		return <AdminResultsViewComponent
				  counties={this.prepareCounties()}
				  districtsSelect ={this.districtsSelect()}
				  countiesSelect={this.countiesSelect()}
				  onKeyUp={this.onKeyUp}
				  clearQuery={this.clearQuery}
				  activeDistrict={this.state.activeDistrict}
			   />
	}
});

module.exports = AdminResultsViewContainer;
