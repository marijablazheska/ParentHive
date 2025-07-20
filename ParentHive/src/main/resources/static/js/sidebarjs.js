function onCategorySelect(value) {
    if (value) {
        window.location.href = `/tags?tag=${encodeURIComponent(value)}`;
    }
}
