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
}

function dragenter(event) {
	event.preventDefault();
}

class RankingDrop extends HTMLElement {
	constructor() {
		super();
		this.ondragenter = dragenter;
		this.ondragover = dragover;
		var rankingDrop = this;
		this.ondrop = function(event) {
			event.preventDefault();
			var data = event.dataTransfer.getData("text/plain");
			rankingDrop.parentNode.dropped(this, data);
		};
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

		this.appendChild(document.createElement("ranking-drop"));
		for(const rank of data) {
			const drop = document.createElement("ranking-drop")
			drop.load(rank);
			this.appendChild(drop);
			this.appendChild(document.createElement("ranking-drop"));
		}
	}

	dropped(where, what) {
		var ranking = [];
		for(const drop of this.children) {
			var level = [];
			for(const elem of drop.children) {
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
