let counter = 0;

	let breakers = [];
	let editor = document.querySelector('#mytextarea');
	let pageHeight = 29.7*37.795;

	const editorObserver = new ResizeObserver((entries)=>{
		console.log('changing');
		let height = entries[0].contentRect.height;
		let width = entries[0].contentRect.width;
		if((height> (breakers.length+1)*pageHeight ) ){

			let hrElement = document.createElement('hr');
			hrElement.style.cssText = `border:2px dashed blue; position:absolute; top: ${height}px; width:90%; left: ${width*1/20}px; margin-top: ${30}px;`;
			document.body.appendChild(hrElement);
			breakers.push(hrElement);

		}

		if((height<(breakers.length * pageHeight))){

			let hrElement = breakers.pop();
			hrElement.remove();

		}



	});

	editorObserver.observe(document.body);