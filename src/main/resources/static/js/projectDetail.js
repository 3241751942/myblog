const API = "/api/projects";

async function loadProjectDetail() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");

    const container = document.getElementById("detailContainer");
    const title = document.getElementById("mainTitle");

    if (!id || isNaN(id)) {
        container.innerHTML = "<p style='text-align:center; padding:3rem;'>无效项目ID</p>";
        return;
    }

    try {
        const res = await fetch(`${API}/${id}`);
        const project = await res.json();
        title.innerText = project.title;

        // 技术栈
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

        // 设计思路
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

        // ===================== ✅ 最终版 在线演示（截图+按钮，最好看最稳定）=====================
        const demoSection = `
        <div class="section" id="demo">
            <h2><i class="fas fa-desktop"></i> 项目演示</h2>
            <p>点击下方按钮查看项目演示与源代码</p>

            <!-- 项目截图预览（美观、专业、永不报错） -->
            <div style="margin:1.2rem 0; border-radius:10px; overflow:hidden; border:1px solid var(--border-color);">
                <img 
                    src="${project.imageUrl || 'https://picsum.photos/1200/600?random='+id}" 
                    alt="项目截图" 
                    style="width:100%; height:auto; display:block;">
            </div>

            <!-- 演示按钮 -->
            <div style="display:flex; gap:10px; flex-wrap:wrap; margin-top:0.5rem;">
                <a href="${project.demoUrl || 'javascript:void(0)'}" target="_blank" class="btn" 
                   style="${!project.demoUrl ? 'background:#ccc;cursor:not-allowed;' : ''}">
                    <i class="fas fa-external-link-alt"></i> 在线演示
                </a>
                <a href="${project.githubUrl || 'javascript:void(0)'}" target="_blank" class="btn" 
                   style="background:#333; ${!project.githubUrl ? 'opacity:0.6;cursor:not-allowed;' : ''}">
                    <i class="fab fa-github"></i> 查看源码
                </a>
            </div>
        </div>`;

        // 渲染页面
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

            ${demoSection}
        `;

        initNavSwitch();

    } catch (e) {
        console.error(e);
        container.innerHTML = "<p style='text-align:center; padding:3rem;'>加载失败</p>";
    }
}

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