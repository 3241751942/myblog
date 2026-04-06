// index.js - 修改后的版本
// DOM 元素
const mobileBtn = document.getElementById('mobileMenuBtn');
const navLinksUl = document.getElementById('navLinks');
const navItems = document.querySelectorAll('.nav-item');

// 移动端菜单初始化
function initMobileMenu() {
    if (mobileBtn) {
        mobileBtn.addEventListener('click', () => {
            navLinksUl.classList.toggle('show');
        });
    }

    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth <= 780) {
                navLinksUl.classList.remove('show');
            }
        });
    });
}

// 更新导航高亮
function setActiveNav(activeNavId) {
    navItems.forEach(item => {
        const navVal = item.getAttribute('data-nav');
        if (navVal === activeNavId) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

// 导航点击处理
function bindNavEvents() {
    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            const href = item.getAttribute('href');
            const navVal = item.getAttribute('data-nav');

            // 如果链接有具体的 href 且不是 "#"，则允许正常跳转
            if (href && href !== '#' && href !== 'javascript:void(0)') {
                // 有实际链接地址，允许跳转，不阻止默认行为
                return;
            }

            // 对于没有实际链接的导航项，阻止跳转并处理
            e.preventDefault();
            setActiveNav(navVal);

            if (navVal !== 'home') {
                console.log(`导航到：${navVal}`);
                // 可以在这里添加单页切换逻辑
            }
        });
    });
}

// 初始化页面
function init() {
    initMobileMenu();
    bindNavEvents();
    setActiveNav('home');
}

// 启动应用
init();