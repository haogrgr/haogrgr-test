function get(url) {
	return new Promise(function(resolve, reject) {
		var req = new XMLHttpRequest();
		req.open("GET", url);

		req.onload = function() {
			if (req.status == 200) {
				resolve(req.response);
			} else {
				reject(Error(req.statusText));
			}
		}

		req.onerror = function() {
			reject(Error("Network Error"));
		}

		req.send();
	});
}

function getJSON(url) {
	return get(url).then(JSON.parse).catch(function(err) {
		console.log("getJSON failed for", url, err);
		throw err;
	});
}

function addHtmlToPage(html) {
	$('#content').append(html);
}

function addTextToPage(text) {
	$('#content').append(text);
}

getJSON('/haogrgr-test/mvc/story.json').then(function(story) {
	addHtmlToPage(story.heading);

	return story.chapterUrls.map(getJSON).reduce(
		function(sequence, chapterPromise) {
			return sequence.then(function() {
				return chapterPromise;
			}).then(function(chapter) {
				addHtmlToPage(chapter.html);
			});
		}, Promise.resolve());
}).then(function() {
	addTextToPage("All done");
}).catch(function(err) {
	addTextToPage("Argh, broken: " + err.message);
}).then(function() {
	document.querySelector('#spinner').style.display = 'none';
});