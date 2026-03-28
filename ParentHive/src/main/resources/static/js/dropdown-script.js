function initProfileDropdown() {
    let profileArrow = document.querySelector(".profile-arrow");

    if (profileArrow) {
        let profileWrapper = profileArrow.parentElement;

        // Toggle dropdown on profile click
        profileArrow.onclick = function(e) {
            e.stopPropagation();
            profileWrapper.classList.toggle("open");
        };

        return true;
    }
    return false;
}

// Try to initialize immediately
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initProfileDropdown);
} else {
    initProfileDropdown();
}

// Keep trying every 100ms until it works
let initAttempts = 0;
let initInterval = setInterval(function() {
    if (initProfileDropdown()) {
        clearInterval(initInterval);
    }
    initAttempts++;
    if (initAttempts > 50) {
        clearInterval(initInterval);
    }
}, 100);

// Confirm delete functionality
function confirmDelete(event, formId) {
    event.preventDefault();
    event.stopPropagation();
    const confirmed = confirm("Are you sure you want to delete this post?");
    if (confirmed) {
        document.getElementById(formId).submit();
    }
}

