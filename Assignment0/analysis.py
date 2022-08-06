import matplotlib.pyplot as plt
import numpy as np
import pickle
from matplotlib.lines import Line2D
custom_lines = [Line2D([0], [0], color='red', lw=2),
                Line2D([0], [0], color='black', lw=2)]
def f(W, P):
    return 10 * (W - P**3 - P + 1)/((1- P)*(1 - P**3))

# p = np.linspace(0.5, 0.99 , 300)
# w = np.linspace(0,50 , 300)
W = np.arange(1, 21)  # From 1 to 20, steps of 1. (20 values)
P = np.arange(0.05, 1, 0.05)  # From 0.05 to 0.95 steps of 0.05. (19 values)
P, W = np.meshgrid(P, W)
result = pickle.load(open("result.pkl", "rb"))

result = np.array(result)
Z = f(W,P)
plt.figure(figsize=(10,5))
ax = plt.subplot()
ax.contour(P, W, Z, 500, cmap='autumn')
ax.contour(P,W,result,500,cmap='gray')

plt.title("Contour Plot of Time vs (Width, Probabilty)")
plt.xlabel("Probabilty")
plt.ylabel("Width")
ax.legend(custom_lines, ['Theoretical', 'Experimental'],loc="upper left")

plt.savefig("contour.pdf")
plt.show()