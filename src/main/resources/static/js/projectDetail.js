const API = "/api/projects";

async function loadProjectDetail() {
    // 1. 从 URL ?id= 正确获取项目ID
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    const container = document.getElementById("detailContainer");
    const title = document.getElementById("mainTitle");

    if (!id || isNaN(id)) {
        container.innerHTML = "<p style='text-align:center; padding:3rem;'>无效项目ID</p>";
        return;
    }

    try {
        // ==========================
        // 1. 加载项目信息（正确）
        // ==========================
        const res = await fetch(`${API}/${id}`);
        const project = await res.json();
        title.innerText = project.title;

        // ==========================
        // 2. 加载技术栈（你后端是 /techs）
        // ==========================
        let techTags = '<span class="tech">加载中...</span>';
        try {
            const techRes = await fetch(`${API}/${id}/techs`);
            const techList = await techRes.json();
            techTags = techList?.length
                ? techList.map(t => `<span class="tech">${t.name}</span>`).join('')
                : '<span class="tech">暂无技术</span>';
        } catch (e) {
            techTags = '<span class="tech">加载失败</span>';
        }

        // ==========================
        // 3. 加载设计思路（你的接口）
        // ==========================
        let designHtml = "<p>加载中...</p>";
        try {
            const designRes = await fetch(`${API}/design/${id}`);
            const designList = await designRes.json();

            if (designList?.length) {
                designHtml = "";
                designList.forEach(item => {
                    designHtml += `<p><strong>${item.title}</strong>：${item.content}</p>`;
                });
            } else {
                designHtml = "<p>暂无设计思路</p>";
            }
        } catch (e) {
            designHtml = "<p>暂无设计思路</p>";
        }

        // ==========================
        // 渲染页面
        // ==========================
        container.innerHTML = `
            <div class="section active" id="intro">
                <h2><i class="fas fa-info-circle"></i> 项目介绍</h2>
                <p>${project.description || '暂无介绍'}</p>
            </div>

            <div class="section" id="design">
                <h2><i class="fas fa-sitemap"></i> 设计思路</h2>
                ${designHtml}
            </div>

            <div class="section" id="tech">
                <h2><i class="fas fa-code"></i> 技术栈</h2>
                <div class="tech-list">${techTags}</div>
            </div>

            <div class="section" id="demo">
                <h2><i class="fas fa-desktop"></i> 在线演示</h2>
                <a href="${project.demoUrl || '#'}" target="_blank" class="btn">打开演示</a>
                <a href="${project.githubUrl || '#'}" target="_blank" class="btn" style="margin-left:8px; background:#333;">GitHub</a>
            </div>
        `;

        initNavSwitch();

    } catch (e) {
        console.error(e);
        container.innerHTML = "<p style='text-align:center; padding:3rem;'>加载失败</p>";
    }
}

// 导航切换（你原来的逻辑，完全不变）
function initNavSwitch() {
    document.querySelectorAll(".nav-item").forEach(item => {
        item.addEventListener("click", () => {
            document.querySelectorAll(".nav-item").forEach(i => i.classList.remove("active"));
            item.classList.add("active");
            const target = item.dataset.target;
            document.querySelectorAll(".section").forEach(sec => sec.classList.remove("active"));
            document.getElementById(target).classList.add("active");
        });
    });
}

loadProjectDetail();