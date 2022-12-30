package readers.module;

public abstract class Template<T> {
	
	private T finalProduct;
	
	public void setFinalProduct(T finalProduct) {
		this.finalProduct = finalProduct;
	}
	
	public T getFinalProduct() {
		return finalProduct;
	}
	
	public abstract void create();

}
