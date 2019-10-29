import React from 'react';
import { DeferredSpinner } from "sonar-components";
import { findJMetricsForProject } from '../../common/api'
import $ from 'jquery';

export default class JMetricsApp extends React.PureComponent {
	constructor(props) {
	    super(props);
	}
	
	state = {
	  	loading: true,
		data: []
	};

	reader = new FileReader();
	
	exportToExcel(e){
		e.preventDefault();
		window.open(this.reader.result, '_blank');
	}
	
	b64toBlob(b64Data, contentType, sliceSize) {
	    contentType = contentType || '';
	    sliceSize = sliceSize || 512;
	    var byteCharacters = atob(b64Data);
	    var byteArrays = [];
	    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
	        var slice = byteCharacters.slice(offset, offset + sliceSize);
	        var byteNumbers = new Array(slice.length);
	        for (var i = 0; i < slice.length; i++) {
	            byteNumbers[i] = slice.charCodeAt(i);
	        }
	        var byteArray = new Uint8Array(byteNumbers);
	        byteArrays.push(byteArray);
	    }
	    return new Blob(byteArrays, {type: contentType});
	}
		
	componentDidMount() {
		findJMetricsForProject(this.props.project).then(
			data => {
				this.setState({
					loading: false,
					data
				});
			}
		);
		
		window.handleAccordion = function (e) {
			var target = e.target.closest('a');
			var isAlreadyActive = target.classList.contains('active');
	        var currentAttrValue = target.getAttribute("href");
	        var previousActiveElement =  $('.accordion-section-title.active');
	        $('.accordion-section-title.active').removeClass('active');
	        $('.accordion-section-content.open').slideUp(100, function() {
	        	if (isAlreadyActive) {
	        		let elementToScrollTo = previousActiveElement.parent().prevAll('.accordion-section:first');
	        		if (elementToScrollTo.offset() == undefined){
	        			elementToScrollTo = previousActiveElement;
	        		}	
	                $('html, body').animate({
	                    scrollTop: elementToScrollTo.offset().top - 200
	                }, 100);
	        	}
            }).removeClass('open');
	        if(!isAlreadyActive) {
	        	target.classList.add('active');
	            $(currentAttrValue).slideDown(100, function() {
                	let elementToScrollTo = $('.accordion-section-title.active').parent().prevAll('.accordion-section:first');
	        		if (elementToScrollTo.offset() == undefined){
	        			elementToScrollTo = $('.accordion-section-title.active');
	        		}	
	                $('html, body').animate({
	                    scrollTop: elementToScrollTo.offset().top - 200
	                }, 100);
	            }).addClass('open');
	        } 
	        return false;
	    };   
	}		
	render() {
		if (this.state.loading) {
	    	return <div className="page page-limited"><DeferredSpinner /></div>;
	    }
		
		var blob = this.b64toBlob(this.state.data.jmetricsExcel,'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
		this.reader.readAsDataURL(blob);
				
		return (
			<div class="dashbox withSidesPadding">
				<table class="width100">
				   <tbody>
				      <tr>
				         <td class="jmetrics-row">
				            <table>
				               <tbody>
				                  <tr>
				                     <td class="large-label">
				                        <span class="small-margin">jMetrics</span>
				                     </td>
				                     <td>
				                        <a href="#" id="exportToExcel" class="button medium-button small-margin" onClick={e => this.exportToExcel(e)}>Export To Excel</a>
				                     </td>
				                  </tr>
				                  <tr>
				                     <td colspan="2" dangerouslySetInnerHTML={{ __html: this.state.data.jmetricsHtml}} />
				                  </tr>
				               </tbody>
				            </table>
				         </td>
				      </tr>
				   </tbody>
				</table>
			</div>
		);
	}
}