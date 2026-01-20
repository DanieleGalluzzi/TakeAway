/* MENU MOBILE */
const menuToggle = document.getElementById("menuToggle");
const sidebar = document.getElementById("sidebar");

menuToggle?.addEventListener("click", () => {
    sidebar.classList.toggle("aperta");
});

/* ANIMAZIONI SEZIONI */
const sezioni = document.querySelectorAll(".sezione");

const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
        entry.target.classList.toggle("attiva", entry.isIntersecting);
    });
}, { threshold: 0.2 });

sezioni.forEach(sezione => observer.observe(sezione));

/* TORNA SU */
document.getElementById("scrollTopBtn")?.addEventListener("click", () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
});

/* MODALE IMMAGINI */
const modal = document.getElementById("imageModal");
const modalImg = document.getElementById("modalImg");
const closeBtn = document.querySelector(".close-modal");

document.querySelectorAll("#preview img").forEach(img => {
    img.addEventListener("click", () => {
        modal.style.display = "block";
        modalImg.src = img.src;
    });
});

closeBtn?.addEventListener("click", () => modal.style.display = "none");
modal?.addEventListener("click", e => {
    if (e.target === modal) modal.style.display = "none";
});
