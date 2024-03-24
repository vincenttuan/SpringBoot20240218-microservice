const REMOTE_URL = 'http://localhost'; // 遠端 API 伺服器的網址

// 取得指定資源並渲染到指定容器中
const fetchAndRenderData = async(url, containerId, renderFn) => {
	url = REMOTE_URL + url;
	const response = await fetch(url);
	const {success, message, data} = await response.json();
	console.log(data);
	$(containerId).innerHTML = Array.isArray(data) ? data.map(renderFn).join('') : data.map(renderFn);
};

const renderOrders = (order) => {
    // 對於每個訂單中的商品明細，生成對應的 HTML 字串
    const itemsHtml = order.itemDtos.map(item => `
        <li>
            ${item.product.name} - 數量: ${item.quantity}, 單價: ${item.product.price}
        </li>
    `).join('');

    // 組合整個訂單的資訊與商品明細
    return `
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">訂單編號：${order.id}</h5>
                <p class="card-text">訂單日期：${order.orderDate}</p>
                <p class="card-text">客戶名稱：${order.customer.name}</p>
                <p class="card-text">客戶郵箱：${order.customer.email}</p>
                <p class="card-text">訂單商品明細：</p>
                <ul>${itemsHtml}</ul>
            </div>
        </div>
    `;
};


const $ = (id) => {
	return document.getElementById(id);
};

// 等待 DOM 加載完畢後再執行
document.addEventListener("DOMContentLoaded", async() => {
	fetchAndRenderData('/orders', 'orders', renderOrders)
});
