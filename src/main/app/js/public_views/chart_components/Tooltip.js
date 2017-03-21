var React = require('react');

var Tooltip = function(props) {

    let tooltip = props.tooltip
    let visibility = 'hidden'
    let width = 200
    let height = (tooltip.data.length + 1) * 30

    let transform = ''
    let transformText = 'translate(10,10)';
    let transformArrow= '';
    let arrowHeight = 10;
    let x = 0
    let y = 0

    if (tooltip.display == true){
        visibility = 'visible';
        x = tooltip.pos.x;
        y = tooltip.pos.y;

        if (y > height + arrowHeight) {
            transform = 'translate(' + (x - width / 2) + ',' + (y - height - arrowHeight) + ')';
            transformArrow = 'translate(' + (width / 2 - 20) + ',' + (height - 2) + ')';
        } else {
            transform = 'translate(' + (x - width / 2) + ',' + (Math.round(y) + arrowHeight + tooltip.barHeight) + ')';
            transformArrow = 'translate(' + (width / 2 - 20) + ',' + 0 + ') rotate(180,20,0)';
        }
    } else {
        visibility='hidden'
    }

    return (
        <g transform={transform}>
            <rect className='shadow' width={width} height={height} rx='5' ry='5' visibility={visibility} fill='#404040' opacity='.8'/>
            <polygon className='shadow' points='10,0  30,0  20,10' transform={transformArrow}
                     fill='black' opacity='.7' visibility={visibility}/>
            <text dominantBaseline='hanging' visibility={visibility} transform={transformText}>
                <tspan x='0' textAnchor='start' fontSize='17px' fill='white'>{tooltip.label}</tspan>
                {tooltip.data.map(function (d, idx) {
                    return <tspan key={idx} x='0' textAnchor='start' dy='25' fontSize='15px' fill='#f2f2f2'>{d.key + ': ' + d.value}</tspan>
                })}
            </text>
        </g>
    );
}

Tooltip.propTypes = {
    tooltip: React.PropTypes.object
}

module.exports = Tooltip;
