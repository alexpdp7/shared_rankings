function dragstart(event) {
	event.dataTransfer.setData("text/plain", event.target.id);
	event.dataTransfer.dropEffect = "move";
}

class RankingElement extends HTMLElement {
	constructor() {
		super();
		this.draggable = true;
		this.ondragstart = dragstart;
	}

	load(data) {
		this.innerText = data;
		this.id = data;
	}
};

customElements.define("ranking-element", RankingElement);

function dragover(event) {
	event.preventDefault();
	event.dataTransfer.dropEffect = "move";
	this.classList.add("dragHover");
}

function dragenter(event) {
	event.preventDefault();
}

function dragleave(event) {
	this.classList.remove("dragHover");
}

class RankingDrop extends HTMLElement {
	constructor() {
		super();
		this.ondragenter = dragenter;
		this.ondragover = dragover;
		this.ondragleave = dragleave;
		var rankingDrop = this;
		this.ondrop = function(event) {
			event.preventDefault();
			var data = event.dataTransfer.getData("text/plain");
			rankingDrop.parentNode.dropped(this, data);
		};
		const adder = document.createElement("button");
		adder.innerText = "+";
		adder.onclick = function() {
			const newElement = window.prompt();
			if(newElement) {
				rankingDrop.parentNode.dropped(rankingDrop, newElement);
			}
		};
		this.appendChild(adder);
	}

	load(data, rankingRoot) {
		for(const elem of data) {
			const element = new RankingElement("ranking-element");
			element.load(elem);
			this.appendChild(element);
		}
	}
};

customElements.define("ranking-drop", RankingDrop);

class RankingRoot extends HTMLElement {
	constructor() {
		super();
	}

	load(data) {
		while (this.firstChild) {
			this.removeChild(this.firstChild);
		}

		this.appendChild(new RankingDrop());
		for(const rank of data) {
			const drop = new RankingDrop();
			drop.load(rank);
			this.appendChild(drop);
			this.appendChild(new RankingDrop());
		}
	}

	dropped(where, what) {
		var ranking = [];
		for(const drop of this.children) {
			var level = [];
			for(const elem of drop.querySelectorAll("ranking-element")) {
				if(elem.id == what) {
					continue;
				}
				level.push(elem.id);
			}
			if(drop == where) {
				level.push(what);
			}
			if(level.length != 0) {
				ranking.push(level);
			}
		}
		var xhr = new XMLHttpRequest();
		xhr.open("POST", document.URL + "/update");
		xhr.overrideMimeType("text/plain");
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send(JSON.stringify(ranking));
	}
};

customElements.define("ranking-root", RankingRoot);
