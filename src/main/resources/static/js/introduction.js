const mobileBtn = document.getElementById('mobileMenuBtn');
const navLinksUl = document.getElementById('navLinks');
if(mobileBtn){
    mobileBtn.addEventListener('click', () => {
        navLinksUl.classList.toggle('show');
    });
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', () => {
            if (window.innerWidth <= 780) {
                navLinksUl.classList.remove('show');
            }
        });
    });
}