const url = "ws://localhost:9902/spring-boot-websocket";

const client = new StompJs.Client({
    brokerURL: url
});

var subscriptions = {}; // 存儲訂閱的物件

client.onConnect = (frame) => {
    console.log('Connected: ' + frame);
};

client.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

client.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

const submitSubscribe = async () => {
	const symbol = symbolInput.value;
	
	if (symbol == '') {
		alert('請輸入股票代號');
		return;
	}
	
	subscribe(symbol);
}

const subscribe = async (symbol) => {
	const topic = "/topic/" + symbol;
	
	
	symbolInput.value = '';
	
	if (subscriptions[symbol]) {
		//alert(symbol + '已經訂閱過了');
		return;
	}
	    
	// 利用 fetch await 先透過 http://localhost:9902/price/{symbol} 取得最新的 quote
	const response = await fetch('http://localhost:9902/price/' + symbol);
	if(response != null) {
		var quote = await response.json();
		displayQuote(symbol, quote);
		console.log(quote);
	}
	
	client.subscribe(topic, (message) => {
	    displayQuote(symbol, JSON.parse(message.body));
	});

	subscriptions[symbol] = true;
};

function displayQuote (symbol, quote) {
    // 檢查是否已存在該 ID 的 div
    var existingId = document.getElementById(symbol);
    
    // 格式化時間 HH:mm:ss
    quote.matchTime = quote.matchTime.substring(0, 2) + ':' + quote.matchTime.substring(2, 4) + ':' + quote.matchTime.substring(4, 6);
    
    if (existingId) {
        // 如果 id 已存在，更新內容
        // 若 quote.lastPrice = null 則該欄位不更新
        existingId.cells[0].innerHTML = quote.symbol;
        if (quote.lastPrice != null) {
			existingId.cells[1].innerHTML = quote.lastPrice;
		}
		if (quote.lastVolume != null) {
			existingId.cells[2].innerHTML = quote.lastVolume;
		}
        existingId.cells[3].innerHTML = quote.bidPrices[0];
        existingId.cells[4].innerHTML = quote.bidVolumes[0];
        existingId.cells[5].innerHTML = quote.askPrices[0];
        existingId.cells[6].innerHTML = quote.askVolumes[0];
        existingId.cells[7].innerHTML = quote.matchTime;
        //existingId.innerHTML = '<td>' + quote.symbol + '</td><td>' + quote.lastPrice + '</td><td>' + quote.lastVolume + '</td><td>' + quote.bidPrices[0] + '</td><td>' + quote.bidVolumes[0] + '</td><td>' + quote.askPrices[0] + '</td><td>' + quote.askVolumes[0] + '</td><td>' + quote.matchTime + '</td>';
    	
    	// 使背景閃爍
        existingId.classList.add('flash');
		existingId.addEventListener('animationend', () => {
            existingId.classList.remove('flash');
        });
    } else {
        // 如果 id 不存在，創建一個新的 tr
        var tr = document.createElement('tr');
        tr.id = symbol; // 設定 id
        tr.innerHTML = '<td>' + quote.symbol + '</td><td>' + quote.lastPrice + '</td><td>' + quote.lastVolume + '</td><td>' + quote.bidPrices[0] + '</td><td>' + quote.bidVolumes[0] + '</td><td>' + quote.askPrices[0] + '</td><td>' + quote.askVolumes[0] + '</td><td>' + quote.matchTime + '</td>';
        receiveBody.appendChild(tr); 
        // 使背景閃爍
        tr.classList.add('flash');
        tr.addEventListener('animationend', () => {
            tr.classList.remove('flash');
        });
    }
}

document.addEventListener("DOMContentLoaded", function() {
	client.activate();
	console.log('WebSocket client activated');
	
	const symbolInput = document.getElementById('symbolInput');
	const subscribeBtn = document.getElementById('subscribeBtn');
	const form = document.getElementById('myForm');
	const receiveBody = document.getElementById('receiveBody');
	
	// 防止表单提交
	form.addEventListener('submit', function(event) {
	    event.preventDefault();
	});
	
	subscribeBtn.addEventListener('click', function () {
		event.preventDefault();
		submitSubscribe();
	});
	
	// symbolInput 按下 enter 時，觸發 subscribe
	symbolInput.addEventListener('keyup', function (event) {
		if (event.keyCode === 13) {
			event.preventDefault();
			submitSubscribe();
		}
	});
	
	// 預設訂閱 2330, 2303, 3008, 2317, 0050, 1101, 1402, 00632R
	//const defaultSymbols = ['2330', '2303', '3008', '2317', '0050', '1101', '1402', '00632R', '2884', '2891', '3231'];
	const defaultSymbols = ['2330', '2303'];
	defaultSymbols.forEach((symbol) => {
		//  避免訂閱太快，導致後端還沒訂閱完成
		setTimeout(() => {
			subscribe(symbol);
		}, 50);
	});
	
});

