const API_BASE_URL = 'http://localhost:8080/api';

// DOM 元素
const projectsContainer = document.getElementById('projectsContainer');
const filterChips = document.querySelectorAll('.filter-chip');
const searchInput = document.getElementById('searchInput');
let currentCategory = 'all';
let currentSearch = '';
let allProjects = [];

// 从后端获取项目数据
async function fetchProjects() {
    try {
        // 显示加载状态
        projectsContainer.innerHTML = `
            <div class="no-results" style="grid-column: 1/-1;">
                <i class="fas fa-spinner fa-spin" style="font-size: 3rem; color: #2563eb; margin-bottom: 1rem; display: block;"></i>
                <h3>加载中...</h3>
            </div>
        `;

        const response = await fetch(`${API_BASE_URL}/projects`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const data = await response.json();

        // 转换后端数据格式为前端需要的格式
        allProjects = data.map(project => ({
            id: project.id,
            title: project.title,
            description: project.description,
            category: project.category,
            tech: project.techs?.map(t => t.name) || [],
            image: project.imageUrl,
            demoUrl: project.demoUrl,
            githubUrl: project.githubUrl,
            date: project.date ? project.date.substring(0, 7) : ''
        }));

        console.log('加载到的项目数量:', allProjects.length);  // 调试用
        renderProjects();
    } catch (error) {
        console.error('获取项目数据失败:', error);
        projectsContainer.innerHTML = `
            <div class="no-results" style="grid-column: 1/-1;">
                <i class="fas fa-exclamation-triangle" style="font-size: 3rem; color: #ef4444; margin-bottom: 1rem; display: block;"></i>
                <h3>加载失败</h3>
                <p style="color: #64748b;">请确保后端服务已启动</p>
                <button onclick="fetchProjects()" style="margin-top: 1rem; padding: 0.5rem 1rem; background: #2563eb; color: white; border: none; border-radius: 8px; cursor: pointer;">重试</button>
            </div>
        `;
    }
}

// 渲染项目卡片（修改为使用 allProjects，并添加点击跳转）
function renderProjects() {
    let filtered = allProjects.filter(project => {
        // 分类筛选
        const categoryMatch = currentCategory === 'all' || project.category === currentCategory;
        // 搜索筛选
        const searchMatch = currentSearch === '' ||
            project.title.toLowerCase().includes(currentSearch.toLowerCase()) ||
            project.description.toLowerCase().includes(currentSearch.toLowerCase()) ||
            (project.tech && project.tech.some(t => t.toLowerCase().includes(currentSearch.toLowerCase())));
        return categoryMatch && searchMatch;
    });

    if (filtered.length === 0) {
        projectsContainer.innerHTML = `
            <div class="no-results" style="grid-column: 1/-1;">
                <i class="fas fa-search" style="font-size: 3rem; color: #cbd5e1; margin-bottom: 1rem; display: block;"></i>
                <h3 style="margin-bottom: 0.5rem;">未找到相关项目</h3>
                <p style="color: #64748b;">尝试调整筛选条件或搜索关键词</p>
            </div>
        `;
        return;
    }

    const categoryMap = {
        frontend: '前端项目',
        fullstack: '全栈项目',
        tools: '工具/库',
        mobile: '移动端'
    };

    const projectsHTML = filtered.map(project => `
        <div class="project-card" data-id="${project.id}" style="cursor: pointer;">
            <div class="project-img" style="background-image: url('${project.image || 'https://picsum.photos/id/1/400/200'}');">
                <span class="project-tag">${categoryMap[project.category] || project.category}</span>
            </div>
            <div class="project-content">
                <h3 class="project-title">${escapeHtml(project.title)}</h3>
                <p class="project-desc">${escapeHtml(project.description)}</p>
                <div class="tech-stack">
                    ${project.tech.map(tech => `<span class="tech-badge">${escapeHtml(tech)}</span>`).join('')}
                </div>
                <div class="project-meta">
                    <span style="font-size: 0.75rem; color: #94a3b8;">
                        <i class="far fa-calendar-alt"></i> ${project.date}
                    </span>
                    <div class="project-links">
                        <a href="${project.demoUrl || '#'}" target="_blank" class="demo-link" onclick="event.stopPropagation();">
                            <i class="fas fa-external-link-alt"></i> 演示
                        </a>
                        <a href="${project.githubUrl || '#'}" target="_blank" onclick="event.stopPropagation();">
                            <i class="fab fa-github"></i> 源码
                        </a>
                    </div>
                </div>
            </div>
        </div>
    `).join('');

    projectsContainer.innerHTML = projectsHTML;

    // 绑定卡片点击事件
    document.querySelectorAll('.project-card').forEach(card => {
        card.addEventListener('click', () => {
            const projectId = card.getAttribute('data-id');
            window.location.href = `projectDetail.html?id=${projectId}`;
        });
    });
}

// 防XSS攻击
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// 更新筛选高亮
function updateFilterUI() {
    filterChips.forEach(chip => {
        const chipCategory = chip.getAttribute('data-category');
        if (chipCategory === currentCategory) {
            chip.classList.add('active');
        } else {
            chip.classList.remove('active');
        }
    });
}

// 筛选点击事件
function bindFilterEvents() {
    filterChips.forEach(chip => {
        chip.addEventListener('click', () => {
            const category = chip.getAttribute('data-category');
            currentCategory = category;
            updateFilterUI();
            renderProjects();
        });
    });
}

// 搜索事件（添加防抖）
let searchTimeout;
function bindSearchEvent() {
    searchInput.addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            currentSearch = e.target.value;
            renderProjects();
        }, 300);
    });
}

// 移动端菜单
function initMobileMenu() {
    const mobileBtn = document.getElementById('mobileMenuBtn');
    const navLinks = document.getElementById('navLinks');
    if (mobileBtn) {
        mobileBtn.addEventListener('click', () => {
            navLinks.classList.toggle('show');
        });
    }
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth <= 780) {
                navLinks.classList.remove('show');
            }
        });
    });
}

// 设置当前导航高亮
function setActiveNav() {
    const currentPath = window.location.pathname.split('/').pop() || 'projects.html';
    const navItems = document.querySelectorAll('.nav-links a');
    navItems.forEach(item => {
        const href = item.getAttribute('href');
        if (href === currentPath || (currentPath === 'projects.html' && href === 'projects.html')) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });
}

// 初始化
function init() {
    bindFilterEvents();
    bindSearchEvent();
    initMobileMenu();
    setActiveNav();
    fetchProjects();  // ✅ 从后端获取数据，而不是直接渲染
}

init();