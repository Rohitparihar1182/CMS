const openModal = document.querySelector("#open-modal");
const closeModal = document.querySelector("#close-modal");
const modalContainer = document.querySelector('.modal-container');
const modal = document.querySelector(".modal");

const ANIMATION_DURATION = 400;

openModal.addEventListener('click', () => {
    modalContainer.classList.remove('hidden');
    const shadowArea = modalContainer.querySelector('.shadow');
    shadowArea.animate([{opacity: "0"}, {opacity: 1}], {duration: ANIMATION_DURATION, easing: "ease-in-out" })
    shadowArea.addEventListener('click', () => {
        handleCloseModal();
    });
});
closeModal.addEventListener('click', () => {
    handleCloseModal();
});

window.addEventListener('keyup', (e) => {
    if (e.key === 'Escape') {
        if(!modalContainer.classList.contains('hidden')) handleCloseModal();
    }
});

function handleCloseModal(){
    modal.animate({transform: "translateY(70vh)"},{duration: ANIMATION_DURATION+150, easing: "linear" });
    const shadowArea = modalContainer.querySelector('.shadow');
    shadowArea.animate({opacity: "0"}, {duration: ANIMATION_DURATION, easing: "ease-in-out" })
    setTimeout(() => {
        modalContainer.classList.add('hidden');
    }, ANIMATION_DURATION);
}
